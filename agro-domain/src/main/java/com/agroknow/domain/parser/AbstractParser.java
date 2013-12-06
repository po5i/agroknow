package com.agroknow.domain.parser;

import com.agroknow.domain.SimpleMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractParser implements Parser {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ObjectMapper mapper;

    private Validator validator;

    public AbstractParser() {}

    public AbstractParser(ObjectMapper mapper, Validator validator) {
        this.mapper = mapper;
        this.validator = validator;
    }

    protected abstract SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException, JsonProcessingException;

    public SimpleMetadata load(String filePath) throws ParserException {
        FileInputStream fileReader = null;
        try {
            fileReader = FileUtils.openInputStream(new File(filePath));
            SimpleMetadata result = loadInternal(fileReader);
            if (validator != null) {
                Set<ConstraintViolation<SimpleMetadata>> constraintViolations = validator.validate(result);
                if (!CollectionUtils.isEmpty(constraintViolations)) {
                    logValidationErrors(constraintViolations);
                    throw new ParserException("Validation Errors");
                }
            }
            return result;
        } catch (JsonProcessingException e1) {
            throw new ParserException("JSON Parser Error [" + e1.getMessage() + "]");
        } catch (IOException e) {
            throw new ParserException("Generic IOError [" + e.getMessage() + "]");
        } 
        finally {
            IOUtils.closeQuietly(fileReader);
        }
    }

    private void logValidationErrors(Set<ConstraintViolation<SimpleMetadata>> constraintViolations) {
        for (ConstraintViolation<SimpleMetadata> constr : constraintViolations) {
            log.error("Constraint error {}", constr);
        }
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * @return the validator
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * @param validator
     *            the validator to set
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}