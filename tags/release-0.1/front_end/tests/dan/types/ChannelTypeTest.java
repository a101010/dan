/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dan.types;

import java.util.ArrayList;
import dan.types.ChannelType;
import dan.types.ChannelType.ChanBehavior;
import dan.types.ChannelType.ChanDepth;
import dan.types.BuiltinType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alan
 */
public class ChannelTypeTest {

    static ChannelType c1;
    static ChannelType c2;

    public ChannelTypeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ArrayList<DanType> p1 = new ArrayList<DanType>();
        BuiltinType int32Type = new BuiltinType(BuiltinType.Builtins.Int32);
        p1.add(int32Type);
        c1 = new ChannelType(p1, ChanDepth.finite, 0, ChanBehavior.block);

        p1 = new ArrayList<DanType>();
        ArrayList<DanType> p2 = new ArrayList<DanType>();
        p2.add(int32Type);
        p1.add(new ChanRType(p2));
        c2 = new ChannelType(p1, ChanDepth.unbounded, 12, ChanBehavior.block);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class ChannelType.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        ChannelType instance = c1;
        String expResult = "channel<int32>(0, block)";
        String result = instance.getName();
        assertEquals(expResult, result);
        System.out.println("c1 result == " + result);

        instance = c2;
        result = instance.getName();
        expResult = "channel<chanr<int32>>(unbounded, block)";
        assertEquals(expResult, result);
        System.out.println("c2 result == " + result);
    }

    /**
     * Test of getChanDepth1 method, of class ChannelType.
     */
    @Test
    public void testGetChanDepth1() {
        System.out.println("getChanDepth1");
        ChannelType instance = c1;
        ChanDepth expResult = ChanDepth.finite;
        ChanDepth result = instance.getChanDepth1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChanDepth2 method, of class ChannelType.
     */
    @Test
    public void testGetChanDepth2() {
        System.out.println("getChanDepth2");
        ChannelType instance = c1;
        int expResult = 0;
        int result = instance.getChanDepth2();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChanBehavior method, of class ChannelType.
     */
    @Test
    public void testGetChanBehavior() {
        System.out.println("getChanBehavior");
        ChannelType instance = c1;
        ChanBehavior expResult = ChanBehavior.block;
        ChanBehavior result = instance.getChanBehavior();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEmittedType method, of class ChannelType.
     */
    @Test
    public void testGetEmittedType() {
        System.out.println("getEmittedType");
        ChannelType instance = c1;
        String expResult = "Channel_int32_0_block";
        String result = instance.getEmittedType();
        assertEquals(expResult, result);
        System.out.println("c1 result == " + result);

        instance = c2;
        result = instance.getEmittedType();
        expResult = "Channel_ChanR_int32_unbounded_block";
        assertEquals(expResult, result);
        System.out.println("c2 result == " + result);
    }

    @Test
    public void testInt() {
        int foo = Integer.parseInt("5");
    }

}