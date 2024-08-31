# Wiki parser
## How to use Stax parser
* https://www.geeksforgeeks.org/stax-xml-parser-java/
* https://www.baeldung.com/java-stax

## Wiki file format
https://en.wikipedia.org/wiki/Help:Wikitext

## Data source
* https://meta.wikimedia.org/wiki/Data_dump_torrents#English_Wikipedia  
Download Simple Wikipedia for smaller data source  

Command to extract part of text from xml (1000 lines):
```
head -1000 simplewiki-20230820-pages-articles-multistream.xml >> test.xml
```