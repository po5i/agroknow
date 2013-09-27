package com.agroknow.linkchecker;

import com.agroknow.linkchecker.domain.FileMetadata;
import com.agroknow.linkchecker.service.FileMetadataService;
import com.agroknow.linkchecker.service.LinkCheckingService;
import com.agroknow.linkchecker.service.RedirectionRulesService;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.jdeferred.DeferredManager;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.impl.DefaultDeferredManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static final Options OPTS = new Options();

    private App() {
        super();
    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final LinkCheckerOptions options = parseOptions(args);
        LOG.info("Starting the linkchecker with the given options {}", options);

        // read files from root-directory
        Collection<File> files = FileUtils.listFiles(new File(options.getRootFolderPath()), new String[] { "json" }, true);
        int filesSize = files.size();

        if (CollectionUtils.isEmpty(files)) {
            LOG.error("The specified rootFolderPath is empty or does not contains any json files. Exiting....");
            return;
        }

        //initialize service instances
        RedirectionRulesService redirectionRulesService = new RedirectionRulesService(options);
        final FileMetadataService fileMetaService = new FileMetadataService(options);
        final LinkCheckingService linkCheckingService = new LinkCheckingService(redirectionRulesService);

        LOG.info("Found {} files to process.", filesSize);

        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        DeferredManager deferredManager = new DefaultDeferredManager(threadPool);

        for(File f : files) {
            try {
                final FileMetadata fileMeta = fileMetaService.readFile(f.getPath());
                if (fileMeta == null) throw new LinkCheckingException("Could not read file "+f.getPath()+" into FileMetadata");

                MetricsRegistryHolder.getCounter("FILES[ALL]").inc();

                // call the checkFileLocations to update the url status in FileMetadata
                deferredManager.when(new Callable<FileMetadata>() {
                    public FileMetadata call() throws Exception {
                        LOG.debug("Start link checking for file: {}", fileMeta.getFilePath());
                        return linkCheckingService.checkFileLocations(fileMeta);
                    }

                // when updated try to move/copy the file to the correct location
                }).done(new DoneCallback<FileMetadata>() {
                    public void onDone(FileMetadata d) {
                        try {
                            LOG.trace("Successfully checked links for file: {}", fileMeta.getFilePath());
                            MetricsRegistryHolder.getCounter( fileMeta.isFailed() ? "FILES[ERROR]" : "FILES[SUCCESS]").inc();

                            if(options.isSupportMode()) {
                                fileMetaService.updateFile(fileMeta);
                            } else {
                                fileMetaService.copyFile(fileMeta.getFilePath(), !fileMeta.isFailed());
                            }
                            LOG.debug("Successfully processed file: {}", fileMeta.getFilePath());
                        } catch (Exception ex) {
                            LOG.error("Failed to move/copy result file: {}", fileMeta.getFilePath(), ex);
                        }
                    }

                // if link checking failed error and continue
                // TODO maybe some metrics should be updated here too
                }).fail(new FailCallback<Throwable>() {
                    public void onFail(Throwable f) {
                        LOG.debug("Failed to check links for file: {}", fileMeta.getFilePath());
                    }
                });
            } catch (Exception ex) {
                MetricsRegistryHolder.getCounter("FILES[MALFORMED]").inc();
                LOG.error("Error reading file {}. Skipping...", f.getPath(), ex);
            }
        }

        MetricsRegistryHolder.report();
        LOG.info("All files processed successfully.");
    }

    public static void printHelp() {
        new HelpFormatter().printHelp("agro-indexer", getOptions(), true);
    }

    public static Options getOptions() {
        if (OPTS.getOptions().size() < 1) {
            OPTS.addOption(new Option(LinkCheckerOptions.SupportedOptions.HELP.getOptionName(), "print this message"));
            OPTS.addOption(Option.builder(LinkCheckerOptions.SupportedOptions.MODE.getOptionName()).argName("mode").hasArg().desc("the mode that the linkchecker is about to run. use 'support' for Suport mode, use 'live' for new incoming files.").required().build());
            OPTS.addOption(Option.builder(LinkCheckerOptions.SupportedOptions.FILE_FORMAT.getOptionName()).argName("fileFormat").hasArg().desc("the file format (AKIF, AGRIF etc) that the linkchecker is going to check.").required().build());
            OPTS.addOption(Option.builder(LinkCheckerOptions.SupportedOptions.ROOT_FOLDER_PATH.getOptionName()).argName("rootFolderPath").hasArg().desc("the folder where the files are located.").required().build());
            OPTS.addOption(Option.builder(LinkCheckerOptions.SupportedOptions.SUCCESS_FOLDER_PATH.getOptionName()).argName("successFolderPath").hasArg().desc("the folder where the OK files will be transfered in case of Active mode.").required(false).build());
            OPTS.addOption(Option.builder(LinkCheckerOptions.SupportedOptions.ERROR_FOLDER_PATH.getOptionName()).argName("errorFolderPath").hasArg().desc("the folder where the NOT OK files will be transfered in case of Active mode.").required(false).build());
            OPTS.addOption(Option.builder(LinkCheckerOptions.SupportedOptions.RULES_PATH.getOptionName()).argName("rulesPath").hasArg().desc("the path for the redirection rules file.").required(false).build());
        }
        return OPTS;
    }

    public static LinkCheckerOptions parseOptions(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = getOptions();

        try {
            CommandLine cli = parser.parse(options, args);

            // if --help, print help and exit
            if(cli.hasOption("help")) {
                printHelp();
                System.exit(0);
            }

            LinkCheckerOptions linkCheckerOptions = LinkCheckerOptions.newInstance(cli, options);
            linkCheckerOptions.validate();

            return linkCheckerOptions;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), (LOG.isDebugEnabled() ? ex : null));
            printHelp();
            System.exit(1);
            return null;
        }
    }
}
