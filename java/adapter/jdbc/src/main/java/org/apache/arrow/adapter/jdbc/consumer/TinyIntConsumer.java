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

package org.apache.arrow.adapter.jdbc.consumer;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.arrow.vector.TinyIntVector;
import org.apache.arrow.vector.complex.impl.TinyIntWriterImpl;
import org.apache.arrow.vector.complex.writer.TinyIntWriter;

/**
 * Consumer which consume tinyInt type values from {@link ResultSet}.
 * Write the data to {@link org.apache.arrow.vector.TinyIntVector}.
 */
public class TinyIntConsumer implements JdbcConsumer {

  private final TinyIntWriter writer;
  private final int columnIndexInResultSet;

  /**
   * Instantiate a TinyIntConsumer.
   */
  public TinyIntConsumer(TinyIntVector vector, int index) {
    this.writer = new TinyIntWriterImpl(vector);
    this.columnIndexInResultSet = index;
  }

  @Override
  public void consume(ResultSet resultSet) throws SQLException {
    byte value = resultSet.getByte(columnIndexInResultSet);
    if (!resultSet.wasNull()) {
      writer.writeTinyInt(value);
    }
    writer.setPosition(writer.getPosition() + 1);
  }
}
