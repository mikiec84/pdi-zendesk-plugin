/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2016 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.trans.steps.zendesk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.loadsave.LoadSaveTester;
import org.pentaho.di.trans.steps.loadsave.validator.FieldLoadSaveValidator;
import org.pentaho.di.trans.steps.zendesk.ZendeskInputIncrementalMeta.IncrementalType;


public class ZendeskInputIncrementalMetaTest {

  @BeforeClass
  public static void setUpBeforeClass() throws KettleException {
    KettleEnvironment.init( false );
  }

  @Test
  public void testRoundTrip() throws KettleException {
    List<String> attributes =
      Arrays.asList( "subDomain", "username", "password", "token",
        "downloadType", "timestampFieldName", "outputFieldName" );

    Map<String, FieldLoadSaveValidator<?>> attributeMap = new HashMap<String, FieldLoadSaveValidator<?>>();
    attributeMap.put( "downloadType", new IncrementalTypeFieldLoadSaveValidator() );

    LoadSaveTester loadSaveTester =
      new LoadSaveTester( ZendeskInputIncrementalMeta.class, attributes, new HashMap<String, String>(),
        new HashMap<String, String>(), attributeMap, new HashMap<String, FieldLoadSaveValidator<?>>() );

    loadSaveTester.testRepoRoundTrip();
    loadSaveTester.testXmlRoundTrip();
  }

  public class IncrementalTypeFieldLoadSaveValidator implements FieldLoadSaveValidator<IncrementalType> {
    @Override
    public IncrementalType getTestObject() {
      int index = new Random().nextInt( IncrementalType.values().length + 1 );
      if ( 0 == index ) {
        return null;
      } else {
        return IncrementalType.values()[( index - 1 )];
      }
    }

    @Override
    public boolean validateTestObject( IncrementalType testObject, Object actual ) {
      if ( testObject == null && actual == null ) {
        return true;
      }
      if ( testObject != null ) {
        return testObject.equals( actual );
      }
      return false;
    }
  }

  @Test
  public void testDefault() {
    ZendeskInputIncrementalMeta meta = new ZendeskInputIncrementalMeta();
    meta.setDefault();
    assertNotNull( meta.getDownloadType() );
    assertEquals( IncrementalType.TICKETS, meta.getDownloadType() );
  }
}
