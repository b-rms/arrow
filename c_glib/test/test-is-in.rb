# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

class TestIsIn < Test::Unit::TestCase
  include Helper::Buildable

  def test_no_null
    left_array = build_int16_array([1, 0, 1, 2])
    right_array = build_int16_array([2, 0])
    assert_equal(build_boolean_array([false, true, false, true]),
                 left_array.is_in(right_array))
  end

  def test_null_in_left_array
    left_array = build_int16_array([1, 0, nil, 2])
    right_array = build_int16_array([2, 0, 3])
    assert_equal(build_boolean_array([false, true, nil, true]),
                 left_array.is_in(right_array))
  end

  def test_null_in_right_array
    left_array = build_int16_array([1, 0, 1, 2])
    right_array = build_int16_array([2, 0, nil, 2, 0])
    assert_equal(build_boolean_array([false, true, false, true]),
                 left_array.is_in(right_array))
  end

  def test_null_in_both_arrays
    left_array = build_int16_array([1, 0, nil, 2])
    right_array = build_int16_array([2, 0, nil, 2, 0, nil])
    assert_equal(build_boolean_array([false, true, true, true]),
                 left_array.is_in(right_array))
  end
end
