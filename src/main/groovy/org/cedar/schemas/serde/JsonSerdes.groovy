package org.cedar.schemas.serde

import groovy.transform.CompileStatic
import org.apache.kafka.common.serialization.Serde

@CompileStatic
class JsonSerdes {

  static Serde<Map> Map() {
    return new JsonMapSerde()
  }

}
