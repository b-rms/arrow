/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.arrow.consumers;

import java.io.IOException;

import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.complex.impl.BitWriterImpl;
import org.apache.arrow.vector.complex.writer.BitWriter;
import org.apache.avro.io.Decoder;

/**
 * Consumer which consume boolean type values from avro decoder.
 * Write the data to {@link BitVector}.
 */
public class AvroBooleanConsumer implements Consumer {

  private final BitWriter writer;
  private final BitVector vector;

  /**
   * Instantiate a AvroBooleanConsumer.
   */
  public AvroBooleanConsumer(BitVector vector) {
    this.vector = vector;
    this.writer = new BitWriterImpl(vector);
  }

  @Override
  public void consume(Decoder decoder) throws IOException {
    writer.writeBit(decoder.readBoolean() ? 1 : 0);
    writer.setPosition(writer.getPosition() + 1);
  }

  @Override
  public void addNull() {
    writer.setPosition(writer.getPosition() + 1);
  }

  @Override
  public void setPosition(int index) {
    writer.setPosition(index);
  }

  @Override
  public FieldVector getVector() {
    return this.vector;
  }

}
