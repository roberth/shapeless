import org.junit.Test
import org.junit.Assert._

class ConversionTests {
  import Tuples._
  import Functions._
  import HList._

  def typed[T](t : => T) {}
  
  @Test
  def testTuples {
    val t1 = (23, "foo", 2.0, true)
    
    val h1 = t1.hlisted
    typed[Int :: String :: Double :: Boolean :: HNil](h1)
    assertEquals(23 :: "foo" :: 2.0 :: true :: HNil, h1)
    
    val h2 = hlisted(t1)
    typed[Int :: String :: Double :: Boolean :: HNil](h2)
    assertEquals(23 :: "foo" :: 2.0 :: true :: HNil, h2)
    
    val l2 = 23 :: "foo" :: 2.0 :: true :: HNil
    
    val t3 = l2.tupled
    typed[(Int, String, Double, Boolean)](t3)
    assertEquals((23, "foo", 2.0, true), t3)
    
    val t4 = tupled(l2)
    typed[(Int, String, Double, Boolean)](t4)
    assertEquals((23, "foo", 2.0, true), t4)
  }
  
  @Test
  def testFunctions {
    val sum : (Int, Int) => Int = _+_
    val prd : (Int, Int, Int) => Int = _*_*_
    
    val hlsum = sum.hlisted
    typed[(Int :: Int :: HNil) => Int](hlsum)
    
    val hlprd = prd.hlisted
    typed[(Int :: Int :: Int :: HNil) => Int](hlprd)
    
    trait A
    trait B extends A
    trait C extends A
    
    val a = new A {}
    val b = new B {}
    
    val ab : A => B = (a : A) => b
    
    val hlab = ab.hlisted
    typed[(A :: HNil) => B](hlab)
    
    def foo[F, L <: HList, R, HF](f : F, l : L)(implicit hl : FnHListerAux[F, HF], ev : HF <:< (L => R)) = hl(f)(l)
    val s2 = foo(sum, 2 :: 3 :: HNil)
    val ab2 = foo(ab, b :: HNil)
  }
  
  @Test
  def testCaseClasses {
    case class Foo(a : Int, b : String, c : Double)
    
    val f1 = Foo(23, "foo", 2.3)
    val t1 = Foo.unapply(f1).get
    val hf = t1.hlisted
    val f2 = Foo.tupled(hf.tupled)
    assertEquals(f1, f2)
  }
}