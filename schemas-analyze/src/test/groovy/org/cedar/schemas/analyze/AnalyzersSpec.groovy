package org.cedar.schemas.analyze

import org.cedar.schemas.avro.geojson.Point
import org.cedar.schemas.avro.psi.*
import org.cedar.schemas.avro.util.AvroUtils
import org.cedar.schemas.parse.ISOParser
import spock.lang.Specification
import spock.lang.Unroll

import java.time.temporal.ChronoUnit

import static org.cedar.schemas.avro.psi.TimeRangeDescriptor.*
import static spock.util.matcher.HamcrestMatchers.closeTo

@Unroll
class AnalyzersSpec extends Specification {

  final String analysisAvro = ClassLoader.systemClassLoader.getResourceAsStream('avro/psi/analysis.avsc').text

  def 'adds an analysis into a parsed record'() {
    def record = ParsedRecord.newBuilder().setType(RecordType.collection).setDiscovery(Discovery.newBuilder().build()).build()

    when:
    def result = Analyzers.addAnalysis(record)

    then:
    result instanceof ParsedRecord
    result.analysis instanceof Analysis
    result.discovery == record.discovery
  }

  def 'analyzing null discovery returns null'() {
    expect:
    Analyzers.analyze(null) == null
  }

  def 'analyzing a default discovery object returns all expected analysis'() {
    def discovery = Discovery.newBuilder().build()

    when:
    def analysis = Analyzers.analyze(discovery)

    then:
    analysis instanceof Analysis
    analysis.identification instanceof IdentificationAnalysis
    analysis.temporalBounding instanceof TemporalBoundingAnalysis
    analysis.spatialBounding instanceof SpatialBoundingAnalysis
    analysis.titles instanceof TitleAnalysis
    analysis.description instanceof DescriptionAnalysis
    analysis.thumbnail instanceof ThumbnailAnalysis
    analysis.dataAccess instanceof DataAccessAnalysis
  }

  def "All valid fields return expected response from service"() {
    given:
    def inputXml = ClassLoader.systemClassLoader.getResourceAsStream('test-iso-metadata.xml').text
    def discovery = ISOParser.parseXMLMetadataToDiscovery(inputXml)

    def expectedAnalysisMap = [
        identification  : [
            fileIdentifierExists    : true,
            fileIdentifierString    : 'gov.super.important:FILE-ID',
            doiExists               : true,
            doiString               : 'doi:10.5072/FK2TEST',
            parentIdentifierExists  : true,
            parentIdentifierString  : 'gov.super.important:PARENT-ID',
            hierarchyLevelNameExists: true,
            isGranule               : true
        ],
        temporalBounding: [
            beginDescriptor         : ValidDescriptor.VALID,
            // For why below value is not seconds, see:
            // https://docs.oracle.com/javase/8/docs/api/java/time/temporal/TemporalQueries.html#precision--
            beginPrecision          : ChronoUnit.NANOS.toString(),
            beginIndexable          : true,
            beginZoneSpecified      : 'Z',
            beginUtcDateTimeString  : '2005-05-09T00:00:00Z',
            endDescriptor           : ValidDescriptor.VALID,
            endPrecision            : ChronoUnit.DAYS.toString(),
            endIndexable            : true,
            endZoneSpecified        : null,
            endUtcDateTimeString    : '2010-10-01T23:59:59.999Z',
            instantDescriptor       : ValidDescriptor.UNDEFINED,
            instantPrecision        : null,
            instantIndexable        : true,
            instantZoneSpecified    : null,
            instantUtcDateTimeString: null,
            rangeDescriptor         : BOUNDED,
        ],
        spatialBounding : [
            spatialBoundingExists: true,
            isValid              : true,
            validationError      : null
        ],
        titles          : [
            titleExists             : true,
            titleCharacters         : 63,
            alternateTitleExists    : true,
            alternateTitleCharacters: 51,
            titleFleschReadingEaseScore: -41.984285714285676,
            alternateTitleFleschReadingEaseScore: 42.61571428571432,
            titleFleschKincaidReadingGradeLevel: 20.854285714285712,
            alternateTitleFleschKincaidReadingGradeLevel: 9.054285714285715
        ],
        description     : [
            descriptionExists    : true,
            descriptionCharacters: 65,
            descriptionFleschReadingEaseScore: 19.100000000000023,
            descriptionFleschKincaidReadingGradeLevel: 12.831111111111113,
        ],
        thumbnail       : [
            thumbnailExists: true,
        ],
        dataAccess      : [
            dataAccessExists: true
        ]
    ]

    when:
    def analysis = Analyzers.analyze(discovery)

    then:
    AvroUtils.avroToMap(analysis.identification) == expectedAnalysisMap.identification
    AvroUtils.avroToMap(analysis.titles) == expectedAnalysisMap.titles
    AvroUtils.avroToMap(analysis.description) == expectedAnalysisMap.description
    AvroUtils.avroToMap(analysis.dataAccess) == expectedAnalysisMap.dataAccess
    AvroUtils.avroToMap(analysis.thumbnail) == expectedAnalysisMap.thumbnail
    AvroUtils.avroToMap(analysis.temporalBounding) == expectedAnalysisMap.temporalBounding
    AvroUtils.avroToMap(analysis.spatialBounding) == expectedAnalysisMap.spatialBounding
  }

  def 'extracts date info from date strings'() {
    when:
    def result = new Analyzers.DateInfo(input, start)
    println(result.utcDateTimeString)

    then:
    result.descriptor == descriptor
    result.precision == precision
    result.indexable == indexable
    result.zoneSpecified == zone
    result.utcDateTimeString == string

    where:
    input                  | start || descriptor                | precision | indexable | zone | string
    '2042-04-02T00:42:42Z' | false || ValidDescriptor.VALID     | 'Nanos'   | true      | 'Z'  | '2042-04-02T00:42:42Z'
    '2042-04-02T00:42:42'  | false || ValidDescriptor.VALID     | 'Nanos'   | true      | null | '2042-04-02T00:42:42Z'
    '2042-04-02'           | false || ValidDescriptor.VALID     | 'Days'    | true      | null | '2042-04-02T23:59:59.999Z'
    '2042-04-02'           | true  || ValidDescriptor.VALID     | 'Days'    | true      | null | '2042-04-02T00:00:00Z'
    '2042-05'              | true  || ValidDescriptor.VALID     | 'Months'  | true      | null | '2042-05-01T00:00:00Z'
    '-2042-05'             | false || ValidDescriptor.VALID     | 'Months'  | true      | null | '-2042-05-31T23:59:59.999Z'
    '2042'                 | true  || ValidDescriptor.VALID     | 'Years'   | true      | null | '2042-01-01T00:00:00Z'
    '1965'                 | false || ValidDescriptor.VALID     | 'Years'   | true      | null | '1965-12-31T23:59:59.999Z'
    '-5000'                | true  || ValidDescriptor.VALID     | 'Years'   | true      | null | '-5000-01-01T00:00:00Z'
    '-3000'                | false || ValidDescriptor.VALID     | 'Years'   | true      | null | '-3000-12-31T23:59:59.999Z'
    '-100000001'           | true  || ValidDescriptor.VALID     | 'Years'   | false     | null | '-100000001-01-01T00:00:00Z'
    '-100000002'           | false || ValidDescriptor.VALID     | 'Years'   | false     | null | '-100000002-12-31T23:59:59.999Z'
    'ABC'                  | true  || ValidDescriptor.INVALID   | null      | false     | null | null
    ''                     | true  || ValidDescriptor.UNDEFINED | null      | true      | null | null
    null                   | true  || ValidDescriptor.UNDEFINED | null      | true      | null | null
  }

  def "#descriptor date range correctly identified when #situation"() {
    given:
    def bounding = TemporalBounding.newBuilder()
        .setBeginDate(begin)
        .setEndDate(end)
        .setInstant(instant)
        .build()
    def discovery = Discovery.newBuilder().setTemporalBounding(bounding).build()

    when:
    def result = Analyzers.analyzeTemporalBounding(discovery)

    then:
    result.rangeDescriptor == descriptor

    where:
    descriptor     | situation                                                   | begin                  | end                    | instant
    ONGOING        | 'begin date exists but not end date'                        | '2010-01-01'           | ''                     | null
    BOUNDED        | 'begin and end date exist and are valid'                    | '2000-01-01T00:00:00Z' | '2001-01-01T00:00:00Z' | null
    UNDEFINED      | 'begin, end, and instant date all undefined'                | ''                     | ''                     | null
    INSTANT        | 'neither begin nor end date exist but valid instant does'   | ''                     | ''                     | '2001-01-01'
    INVALID        | 'end date exists but not begin date, valid instant present' | ''                     | '2010'                 | '2001-01-01'
    INVALID        | 'end date exists but not begin date, instant undefined'     | ''                     | '2010'                 | null
    BACKWARDS      | 'begin and end date exist but start after end'              | '2100-01-01T00:00:00Z' | '2002-01-01'           | null
    NOT_APPLICABLE | 'invalid instant present'                                   | ''                     | ''                     | '2001-01-32'
    NOT_APPLICABLE | 'invalid begin date present'                                | '2001-01-32'           | ''                     | ''
    NOT_APPLICABLE | 'invalid end date present'                                  | ''                     | '2001-01-32'           | ''
    AMBIGUOUS      | 'has valid begin, end, and instant'                         | '2010-01-01'           | '2001-01-01T00:00:00Z' | '2001-01-31'
    AMBIGUOUS      | 'begin date and instant exist but not end date'             | '2010-01-01'           | ''                     | '2001-01-31'
    INSTANT        | 'begin and end date equal timestamps'                       | '2000-01-01T00:00:00Z' | '2000-01-01T00:00:00Z' | null
    BOUNDED        | 'begin and end date same day but times assumed BoD and EoD' | '2010-01-01'           | '2010-01-01'           | null
  }

  def "Range descriptor is #value when #situation"() {
    given:
    def bounding = TemporalBounding.newBuilder()
        .setBeginDate(begin)
        .setEndDate(end)
        .build()
    def discovery = Discovery.newBuilder().setTemporalBounding(bounding).build()

    when:
    def result = Analyzers.analyzeTemporalBounding(discovery)

    then:
    result.rangeDescriptor == value

    where:
    value          | situation                                               | begin                  | end
    BOUNDED        | 'start is valid format and before valid format end'     | '2010-01-01'           | '2011-01-01'
    BACKWARDS      | 'start is valid format and after valid format end'      | '2011-01-01T00:00:00Z' | '2001-01-01T00:00:00Z'
    BOUNDED        | 'start is paleo and before valid format end'            | '-1000000000'          | '2015'
    BOUNDED        | 'start and end both paleo and start before end'         | '-2000000000'          | '-1000000000'
    BOUNDED        | 'start valid LT end valid but years less than 4 digits' | '-900'                 | '100-01-01'
    BACKWARDS      | 'start and end both paleo and start after end'          | '-1000000000'          | '-2000000000'
    INSTANT        | 'start and end both same instant'                       | '2000-01-01T00:00:00Z' | '2000-01-01T00:00:00Z'
    ONGOING        | 'start exists but not end'                              | '2000-01-01T00:00:00Z' | ''
    INVALID        | 'start does not exist but end does'                     | ''                     | '2000-01-01T00:00:00Z'
    UNDEFINED      | 'neither start nor end exist'                           | ''                     | ''
    NOT_APPLICABLE | 'start is paleo and end is invalid'                     | '-1000000000'          | '1999-13-12'
    NOT_APPLICABLE | 'start is invalid and end is paleo'                     | '15mya'                | '-1000000000'
    NOT_APPLICABLE | 'start is valid and end is invalid'                     | '2000-01-01T00:00:00Z' | '2000-12-31T25:00:00Z'
    NOT_APPLICABLE | 'start and end both invalid'                            | '2000-01-01T00:61:00Z' | '2000-11-31T00:00:00Z'
    NOT_APPLICABLE | 'start is invalid but end is valid'                     | '2000-01-01T00:00:61Z' | '2000-01-02T00:00:00Z'
  }

  def "analyzes when links are #testCase"() {
    given:
    def record = Discovery.newBuilder().setLinks(testLinks).build()

    when:
    def dataAccessAnalysis = Analyzers.analyzeDataAccess(record)

    then:
    dataAccessAnalysis instanceof DataAccessAnalysis
    dataAccessAnalysis.dataAccessExists == expected

    where:
    testCase  | testLinks                   | expected
    'missing' | []                          | false
    'present' | [Link.newBuilder().build()] | true
  }

  def "analyzes required identifiers"() {
    given:
    def metadata = Discovery.newBuilder().setFileIdentifier('xyz').build()

    when:
    def result = Analyzers.analyzeIdentifiers(metadata)

    then:
    result instanceof IdentificationAnalysis
    result.fileIdentifierExists
    result.fileIdentifierString == 'xyz'
    !result.doiExists
    result.doiString == null
    !result.parentIdentifierExists
    result.parentIdentifierString == null
    !result.hierarchyLevelNameExists
    !result.isGranule
  }

  def "detects mismatch between metadata type and corresponding identifiers"() {
    given:
    def builder = Discovery.newBuilder()
    builder.fileIdentifier = 'xyz'
    builder.hierarchyLevelName = 'granule'
    def metadata = builder.build()

    when:
    def result = Analyzers.analyzeIdentifiers(metadata)

    then:
    result instanceof IdentificationAnalysis
    result.fileIdentifierExists
    result.fileIdentifierString == 'xyz'
    !result.doiExists
    result.doiString == null
    !result.parentIdentifierExists
    result.parentIdentifierString == null
    result.hierarchyLevelNameExists
    !result.isGranule
  }

  def 'handles analysis of #testCase strings'() {
    when:
    def result = new Analyzers.StringInfo(value)

    then:
    result instanceof Analyzers.StringInfo
    result.exists == exists
    result.characters == length
    result.readingEase == ease
    result.gradeLevel == grade

    where:
    testCase  | value  | exists | length | ease    | grade
    'missing' | null   | false  | 0      | null    | null
    'empty'   | ''     | false  | 0      | null    | null
  }

  def 'analyzes #testCase strings'() {
    when:
    def result = new Analyzers.StringInfo(value)

    then:
    result instanceof Analyzers.StringInfo
    result.exists == exists
    result.characters == length
    closeTo(ease, 0.01).matches(result.readingEase)
    closeTo(grade, 0.01).matches(result.gradeLevel)

    where:
    testCase  | value  | exists | length | ease    | grade
    'present' | 'test' | true   | 4      | 121.220 | -3.40
  }

  def "analyzes when titles are missing"() {
    given:
    def metadata = Discovery.newBuilder().build()

    when:
    def titlesAnalysis = Analyzers.analyzeTitles(metadata)

    then:
    titlesAnalysis instanceof TitleAnalysis
    !titlesAnalysis.titleExists
    titlesAnalysis.titleCharacters == 0
    !titlesAnalysis.alternateTitleExists
    titlesAnalysis.alternateTitleCharacters == 0
  }

  def "analyses when description is missing"() {
    given:
    def metadata = Discovery.newBuilder().build()

    when:
    def descriptionAnalysis = Analyzers.analyzeDescription(metadata)

    then:
    descriptionAnalysis instanceof DescriptionAnalysis
    !descriptionAnalysis.descriptionExists
    descriptionAnalysis.descriptionCharacters == 0
  }

  def "analyzes when thumbnail is #testCase"() {
    given:
    def metadata = Discovery.newBuilder().setThumbnail(value).build()

    when:
    def thumbnailAnalysis = Analyzers.analyzeThumbnail(metadata)

    then:
    thumbnailAnalysis instanceof ThumbnailAnalysis
    thumbnailAnalysis.thumbnailExists == expected

    where:
    testCase  | value                | expected
    'missing' | null                 | false
    'present' | 'thumbnailAnalysis!' | true
  }

  def "analyzes when spatial boundings are #testCase"() {
    given:
    def metadata = Discovery.newBuilder().setSpatialBounding(value).build()

    when:
    def result = Analyzers.analyzeSpatialBounding(metadata)

    then:
    result instanceof SpatialBoundingAnalysis
    result.spatialBoundingExists == expected

    where:
    testCase  | value        | expected
    'missing' | null         | false
    'present' | buildPoint() | true
  }

  static buildPoint() {
    Point.newBuilder()
        .setCoordinates([1 as Double, 2 as Double])
        .build()
  }
}
