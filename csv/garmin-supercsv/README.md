# CSV Converter using Super CSV

## About

Example application that converts and filters
from the CSV format written by the [Garmin Connect Exporter Script][exporter]
into a private CSV format used to import data into
a personal spreadsheet.

## Libraries

Libraries used in this example:

- [Super CSV][supercsv] CSV parser library
- [args4j][args4j] Command line parser library
- [MapStruct][mapstruct] Code generator to map between Java beans

There are sibling example applications using other CSV parser libraries:

- Using [Apache Commons CSV][commonscsv]: https://github.com/pe-st/snppts/tree/master/csv/garmin-commons-csv
- Using [opencsv][opencsv]: https://github.com/pe-st/snppts/tree/master/csv/garmin-opencsv 


[opencsv]: http://opencsv.sourceforge.net/
[commonscsv]: http://commons.apache.org/proper/commons-csv/
[supercsv]: https://super-csv.github.io/super-csv/index.html
[args4j]: http://args4j.kohsuke.org/
[mapstruct]: http://mapstruct.org/
[exporter]: https://github.com/kjkjava/garmin-connect-export 
[exporter-ps]: https://github.com/pe-st/garmin-connect-export 