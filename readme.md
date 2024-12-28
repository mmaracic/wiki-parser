# Wiki parser
## Parsers
StAX and SAX parser can not handle text in prologue or xml files corrupted in some way.
That is why VanillaParser was implemented from scratch, it does no validation and can handle validity problems but parsing is relatively simple right now. 
### How to use Stax parser
* https://www.geeksforgeeks.org/stax-xml-parser-java/
* https://www.baeldung.com/java-stax

### How to use SAX parser
* https://www.baeldung.com/java-sax-parser

## Wiki file format
https://en.wikipedia.org/wiki/Help:Wikitext

### Index format
https://stackoverflow.com/questions/29020732/how-to-use-information-provided-in-wiki-downloads-index-file

### Wiki Markup Cleaner
* https://github.com/lintool/wikiclean

## Data source
* https://meta.wikimedia.org/wiki/Data_dump_torrents#English_Wikipedia  
Download Simple Wikipedia for smaller data source  

Command to extract part of text from xml (1000 lines):
```
head -1000 simplewiki-20230820-pages-articles-multistream.xml >> test.xml
```

## Structure and processing sample
https://dev.to/tobiasjc/processing-the-wikipedia-dump-pf8

## Compression and decompression
Used library:  
https://commons.apache.org/proper/commons-compress/index.html  
Wiki uses multistream bzip2 compression. It means that original file is divided into pieces that are compressed separately
and then combined back together with each compressed piece being a complete, self-contained bzip2 file.    
CompressorInputStream by default reads only the first chunk/stream of data and then halts and needs to be recreated
for second stream or decompressUntilEOF needs ro be set  to true to decompress all the streams continuously.  
https://stackoverflow.com/questions/37702388/reading-a-large-compressed-file-using-apache-commons-compress  
Custom created stream to decompress continuously:  
https://chaosinmotion.com/2011/07/29/and-another-curiosity-multi-stream-bzip2-files/

## How to use application
How to run app from CLI:
```
./gradlew :app:bootRun -Ddb_user=<user> -Ddb_pass=<pass>
```

To search for text containing "april" on local machine use HTTP GET url:
```
http://localhost:8080/wiki/search?title-text=april
```
This will return full articles with "april" in title.