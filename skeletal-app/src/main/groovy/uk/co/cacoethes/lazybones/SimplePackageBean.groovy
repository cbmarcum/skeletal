package uk.co.cacoethes.lazybones

// http://opencsv.sourceforge.net/
// http://opencsv.sourceforge.net/apidocs/index.html
import com.opencsv.CSVReader
import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.CsvBindByName
import groovy.transform.ToString

// bean to read the cad csv into
@ToString(includeNames=true)
class SimplePackageBean {

    @CsvBindByName
    String name

    @CsvBindByName
    String version

    @CsvBindByName
    String owner

    @CsvBindByName
    String description

}
