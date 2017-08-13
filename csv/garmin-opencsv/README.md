# CSV Converter using opencsv

## About

Example application that converts and filters
from the CSV format written by the [Garmin Connect Exporter Script][exporter]
into a private CSV format used to import data into
a personal spreadsheet.

## Libraries

Libraries used in this example:

- [opencsv][opencsv] CSV parser library
- [JCommander][jcmd] Command line parser library
- [MapStruct][mapstruct] Code generator to map between Java beans

There are sibling example applications using other CSV parser libraries:

- Using [Apache Commons CSV][commonscsv]: https://github.com/pe-st/snppts/tree/master/csv/garmin-commons-csv
- Using [Super CSV][supercsv]: https://github.com/pe-st/snppts/tree/master/csv/garmin-supercsv 


[opencsv]: http://opencsv.sourceforge.net/
[commonscsv]: http://commons.apache.org/proper/commons-csv/
[supercsv]: https://super-csv.github.io/super-csv/index.html
[jcmd]: http://www.jcommander.org/
[mapstruct]: http://mapstruct.org/
[exporter]: https://github.com/kjkjava/garmin-connect-export 
[exporter-ps]: https://github.com/pe-st/garmin-connect-export 