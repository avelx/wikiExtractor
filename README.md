# wikiExtractor

Data -> enwiki-20200401-pages-articles.xml.bz2

Spark based job to transform wikipedia xml file.

Overrride config file:
  -Dconfig.file=path/to/config-file
  
  
Typical way to run the latest version:

  ./update-jar.sh ; ./runner.sh
  
Make sure you refer to the latest version in update-jar.sh script.
