/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package org.cedar.schemas.avro.psi;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class KeywordsElement extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1181877770621785638L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"KeywordsElement\",\"namespace\":\"org.cedar.psi.common.avro\",\"fields\":[{\"name\":\"values\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},\"default\":[]},{\"name\":\"type\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null},{\"name\":\"namespace\",\"type\":[\"null\",{\"type\":\"string\",\"avro.java.string\":\"String\"}],\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
   private java.util.List<String> values;
   private String type;
   private String namespace;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public KeywordsElement() {}

  /**
   * All-args constructor.
   * @param values The new value for values
   * @param type The new value for type
   * @param namespace The new value for namespace
   */
  public KeywordsElement(java.util.List<String> values, String type, String namespace) {
    this.values = values;
    this.type = type;
    this.namespace = namespace;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public Object get(int field$) {
    switch (field$) {
    case 0: return values;
    case 1: return type;
    case 2: return namespace;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, Object value$) {
    switch (field$) {
    case 0: values = (java.util.List<String>)value$; break;
    case 1: type = (String)value$; break;
    case 2: namespace = (String)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'values' field.
   * @return The value of the 'values' field.
   */
  public java.util.List<String> getValues() {
    return values;
  }


  /**
   * Gets the value of the 'type' field.
   * @return The value of the 'type' field.
   */
  public String getType() {
    return type;
  }


  /**
   * Gets the value of the 'namespace' field.
   * @return The value of the 'namespace' field.
   */
  public String getNamespace() {
    return namespace;
  }


  /**
   * Creates a new KeywordsElement RecordBuilder.
   * @return A new KeywordsElement RecordBuilder
   */
  public static KeywordsElement.Builder newBuilder() {
    return new KeywordsElement.Builder();
  }

  /**
   * Creates a new KeywordsElement RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new KeywordsElement RecordBuilder
   */
  public static KeywordsElement.Builder newBuilder(KeywordsElement.Builder other) {
    return new KeywordsElement.Builder(other);
  }

  /**
   * Creates a new KeywordsElement RecordBuilder by copying an existing KeywordsElement instance.
   * @param other The existing instance to copy.
   * @return A new KeywordsElement RecordBuilder
   */
  public static KeywordsElement.Builder newBuilder(KeywordsElement other) {
    return new KeywordsElement.Builder(other);
  }

  /**
   * RecordBuilder for KeywordsElement instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<KeywordsElement>
    implements org.apache.avro.data.RecordBuilder<KeywordsElement> {

    private java.util.List<String> values;
    private String type;
    private String namespace;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(KeywordsElement.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.values)) {
        this.values = data().deepCopy(fields()[0].schema(), other.values);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.type)) {
        this.type = data().deepCopy(fields()[1].schema(), other.type);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.namespace)) {
        this.namespace = data().deepCopy(fields()[2].schema(), other.namespace);
        fieldSetFlags()[2] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing KeywordsElement instance
     * @param other The existing instance to copy.
     */
    private Builder(KeywordsElement other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.values)) {
        this.values = data().deepCopy(fields()[0].schema(), other.values);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.type)) {
        this.type = data().deepCopy(fields()[1].schema(), other.type);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.namespace)) {
        this.namespace = data().deepCopy(fields()[2].schema(), other.namespace);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'values' field.
      * @return The value.
      */
    public java.util.List<String> getValues() {
      return values;
    }

    /**
      * Sets the value of the 'values' field.
      * @param value The value of 'values'.
      * @return This builder.
      */
    public KeywordsElement.Builder setValues(java.util.List<String> value) {
      validate(fields()[0], value);
      this.values = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'values' field has been set.
      * @return True if the 'values' field has been set, false otherwise.
      */
    public boolean hasValues() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'values' field.
      * @return This builder.
      */
    public KeywordsElement.Builder clearValues() {
      values = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'type' field.
      * @return The value.
      */
    public String getType() {
      return type;
    }

    /**
      * Sets the value of the 'type' field.
      * @param value The value of 'type'.
      * @return This builder.
      */
    public KeywordsElement.Builder setType(String value) {
      validate(fields()[1], value);
      this.type = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'type' field has been set.
      * @return True if the 'type' field has been set, false otherwise.
      */
    public boolean hasType() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'type' field.
      * @return This builder.
      */
    public KeywordsElement.Builder clearType() {
      type = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'namespace' field.
      * @return The value.
      */
    public String getNamespace() {
      return namespace;
    }

    /**
      * Sets the value of the 'namespace' field.
      * @param value The value of 'namespace'.
      * @return This builder.
      */
    public KeywordsElement.Builder setNamespace(String value) {
      validate(fields()[2], value);
      this.namespace = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'namespace' field has been set.
      * @return True if the 'namespace' field has been set, false otherwise.
      */
    public boolean hasNamespace() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'namespace' field.
      * @return This builder.
      */
    public KeywordsElement.Builder clearNamespace() {
      namespace = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public KeywordsElement build() {
      try {
        KeywordsElement record = new KeywordsElement();
        record.values = fieldSetFlags()[0] ? this.values : (java.util.List<String>) defaultValue(fields()[0]);
        record.type = fieldSetFlags()[1] ? this.type : (String) defaultValue(fields()[1]);
        record.namespace = fieldSetFlags()[2] ? this.namespace : (String) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}