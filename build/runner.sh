#!/bin/sh
set -e

# re-load artifact
perl update.pl

java -Dconfig.file=application.conf -cp "wikiextractor_latest.jar:libs/*" com.avel.data.WikiGroupBy

#java -Dconfig.file=application.conf -cp "wikiextractor_latest.jar:libs/*" com.avel.data.WikiAgg
#java -Dconfig.file=application.conf -cp wiki-extractor.jar com.avel.data.WikiExtractor