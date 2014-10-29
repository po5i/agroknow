How to build everything
=======================

First you have to download this dependency from here https://dl.dropboxusercontent.com/u/24547895/jdeferred.zip and unzip it into  the maven repository (usually /root/.m2/repository/org )

mvn install clean

How to run the linkchecker
==========================

java -jar agro-linkchecker-1.0-SNAPSHOT.jar -errorFolder (error_dir) -format akif -mode live -rootFolder (akif_dir) -rulesPath /path/to/rules.txt -successFolder (success_dir)

How to run the indexer
======================

java -jar agro-indexer-1.0-SNAPSHOT.jar --bulk-size 1000 --root-directory (akif_dir) --runtime-directory /path/to/lastcheck

* where lastcheck is a writable directory.
* first you have to run: 
```
/agroknow/agro-indexer/src/main/resources/elasticsearch# sh AKIF_create_index.sh localhost:9200
```

Auxiliary files
===============

rules.txt content:
```
1, prodinra.inra.fr, 302, #location.url.startsWith('http://prodinra.inra.fr/record/') and #location.redirectsToUrl.startsWith('http://prodinra.inra.fr/?locale=fr#!ConsultNotice'), 200
2, beyondpenguins.ehe.osu.edu, 301, true, 200
```


