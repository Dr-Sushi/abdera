/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  The ASF licenses this file to You
* under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.  For additional information regarding
* copyright in this work, please see the NOTICE file in the top level
* directory of this distribution.
*/
package org.apache.abdera.protocol.client.cache;

import java.security.MessageDigest;

/**
 * Simple cache key based on an md5 hash of the URI.
 * A more complete implementation should take the Vary header into account
 */
public class SimpleCacheKey implements CacheKey {

  private static final long serialVersionUID = 8757289485580165536L;
  private static MessageDigest md;
  static {
    try {
      md = MessageDigest.getInstance("md5");
    } catch (Exception e) {}
  }
  
  private final byte[] key;
  
  public SimpleCacheKey(String uri) {
    MessageDigest md = getMessageDigest();
    synchronized(md) {
      key = getMessageDigest().digest(uri.getBytes());
    }
  }
  
  private static MessageDigest getMessageDigest() {
    return md;
  }
  
  public byte[] getKey() {
    return key;
  }

  public boolean isMatch(CacheKey cacheKey) {
    return (cacheKey instanceof SimpleCacheKey) ?
      MessageDigest.isEqual(
        key, ((SimpleCacheKey)cacheKey).key) : false;
  }
  
  @Override
  public boolean equals(Object obj) {
    return obj instanceof CacheKey ? isMatch((CacheKey)obj) : false;
  }
  
  @Override
  public int hashCode() {
    if (key == null) return 0;
    int result = 1;
    for (byte element : key) {
      result = 31 * result + element;
    }
    return result;
  }

}
