#! /bin/sh

set -e

# re-load artifact
eval('perl update.pl')

java -Dconfig.file=application.conf -cp "wiki-extractor.jar:libs/*" com.avel.data.WikiAgg

#java -Dconfig.file=application.conf -cp wiki-extractor.jar com.avel.data.WikiExtractor