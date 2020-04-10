set -e

if [ -e build/wiki-extractor.jar ];  then
  rm -f 'wiki-extractor.jar'
fi

curl http://192.168.1.8:8081/repository/jerusalemh/com/avel/data/wikiextractor_2.12/0.0.14/wikiextractor_2.12-0.0.15.jar -o wikiextractor.jar