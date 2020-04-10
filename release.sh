set -e

if [ -e build/wiki-extractor.jar ];  then
  rm -f 'build/wiki-extractor.jar'
fi

sbt "clean" "compile" "assembly" "assemblyPackageDependency"

scp -P 2222 -r build pavel@192.168.1.8:/home/pavel/extractor