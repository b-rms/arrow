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

import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.arrow.vector.BitVectorHelper;
import org.apache.arrow.vector.complex.ListVector;

/**
 * Consumer which consume array type values from {@link ResultSet}.
 * Write the data to {@link org.apache.arrow.vector.complex.ListVector}.
 */
public class ArrayConsumer implements JdbcConsumer {

  private final JdbcConsumer delegate;
  private final int columnIndexInResultSet;

  private ListVector vector;

  /**
   * Instantiate a ArrayConsumer.
   */
  public ArrayConsumer(ListVector vector, JdbcConsumer delegate, int index) {
    this.columnIndexInResultSet = index;
    this.delegate = delegate;
    this.vector = vector;
  }

  @Override
  public void consume(ResultSet resultSet) throws SQLException, IOException {
    final Array array = resultSet.getArray(columnIndexInResultSet);
    int idx = vector.getValueCount();
    if (!resultSet.wasNull()) {

      vector.startNewValue(idx);
      int count = 0;
      try (ResultSet rs = array.getResultSet()) {
        while (rs.next()) {
          delegate.consume(rs);
          count++;
        }
      }
      int end = vector.getOffsetBuffer().getInt(idx * 4) + count;
      vector.getOffsetBuffer().setInt((idx + 1) * 4, end);
      BitVectorHelper.setValidityBitToOne(vector.getValidityBuffer(), vector.getValueCount());
    }
    vector.setValueCount(idx + 1);
  }
}
