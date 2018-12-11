package org.cedar.schemas.avro.util

import org.cedar.schemas.avro.psi.Input
import org.cedar.schemas.avro.psi.Method
import org.cedar.schemas.avro.psi.ParsedRecord
import spock.lang.Specification

class AvroUtilsSpec extends Specification {

  def 'transforms an Input into a map'() {
    def testInput = new Input(
        method: Method.POST,
        protocol: 'http',
        host: 'localhost',
        requestUrl: '/test',
        source: 'test',
        contentType: 'application/json',
        content: '{"hello":"world"}'
    )

    expect:
    AvroUtils.avroToMap(testInput) == [
        method: Method.POST,
        protocol: 'http',
        host: 'localhost',
        requestUrl: '/test',
        source: 'test',
        contentType: 'application/json',
        content: '{"hello":"world"}'
    ]
  }

  def 'ParsedRecord has empty array of errors by default'() {
    def record = ParsedRecord.newBuilder().build()

    expect:
    record.errors instanceof List
    record.errors.size() == 0
  }


}
