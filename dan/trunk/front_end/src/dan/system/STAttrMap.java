/*
 * STAttrMap.java
 * 
 */

package dan.system;

import java.util.HashMap;


/** allows convenient multi-value initialization:
 *  "new STAttrMap().put(...).put(...)"
 * 
 *  TODO this is just a copy of a static utility class that ANTLR generages in every
 *  tree walker that uses string templates. It should just be a part of the
 *  ANTLR libraries; file an enhancement request.
 */
public class STAttrMap extends HashMap {
  public STAttrMap put(String attrName, Object value) {
    super.put(attrName, value);
    return this;
  }
  public STAttrMap put(String attrName, int value) {
    super.put(attrName, new Integer(value));
    return this;
  }
}
