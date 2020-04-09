sbt "clean" "compile" "assembly"

scp -P 2222 -r build pavel@192.168.1.8:/home/pavel/extractor