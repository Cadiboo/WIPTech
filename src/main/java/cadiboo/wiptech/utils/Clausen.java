package cadiboo.wiptech.utils;

/**
 * Clausen's integral.
 * <p>
 * This class calculates Clausen's integral defined by
 * <pre>
 *   CLAUSN(x) = integral 0 to x of (-ln(2*sin(t/2))) dt
 * </pre>
 * The code uses Chebyshev expansions with the coefficients
 * given to 20 decimal places.
 * <p>
 * This is a Java port of code originally written in Fortran.
 * The Fortran code this is based upon was last modified 23 January, 1996.
 *
 * @author <a href="mailto:macl_ms0@paisley.ac.uk">Dr. Allan J. MacLeod</a>
 *         (original Fortran implementation)
 * @author <a href="mailto:Martin.vGagern@gmx.net">Martin von Gagern</a>
 *         (Java port)
 *
 * @see <a href="http://dx.doi.org/10.1145/232826.232846">MISCFUN</a>
 */
public class Clausen {

    private static final double PI = 3.1415926535897932385;

    private static final double PISQ = 9.8696044010893586188;

    private static final double TWOPI = 6.2831853071795864769;

    private static final double TWOPIA = 6.28125;

    private static final double TWOPIB = 0.19353071795864769253E-2;

    private static final double[] ACLAUS = {
        2.14269436376668844709E0,
        0.7233242812212579245E-1,
        0.101642475021151164E-2,
        0.3245250328531645E-4,
        0.133315187571472E-5,
        0.6213240591653E-7,
        0.313004135337E-8,
        0.16635723056E-9,
        0.919659293E-11,
        0.52400462E-12,
        0.3058040E-13,
        0.181969E-14,
        0.11004E-15,
        0.675E-17,
        0.42E-18,
        0.3E-19,
    };

    /**
     * The nunber of terms of the array ACLAUS to be used.
     * The recommended value is such that ABS(ACLAUS(NTERMS)) &lt; EPS/100
     * subject to 1 &lt;= NTERMS &lt;= 15
     */
    private static int NTERMS = 13;

    /**
     * The value below which Cl(x) can be approximated by x (1-ln x).
     * The recommended value is pi*sqrt(EPSNEG/2).
     */
    private static final double XSMALL = 2.3406689E-8;

    /**
     * The value of |x| above which we cannot reliably reduce the
     * argument to [0,2*pi]. The recommended value is 1/EPS.
     */
    private static final double XHIGH = 4.5036E15;
    
    /**
     * Calculate Clausen's integral.
     *
     * @param x the upper limit of Clausen's integral
     * @return the value of Clausen's integral
     * @throws ArithmeticException
     *  if |x| is too large so that it is impossible to reduce the argument
     *  to the range [0,2*pi] with any precision
     */
    public static double cl2(double x) {
        double clausn;

        // Error test
        if (Math.abs(x) > XHIGH)
            throw new ArithmeticException("argument too large in size");
        int indx = 1;
        if (x < 0) {
            x = -x;
            indx = -1;
        }

        // Argument reduced using simulated extra precision
        if (x > TWOPI) {
            double t = Math.floor(x/TWOPI);
            x = (x - t*TWOPIA) - t*TWOPIB;
        }
        if (x > PI) {
            x = (TWOPIA - x) + TWOPIB;
            indx = -indx;
        }

        // Set result to zero if X multiple of PI
        if (x <= 0)
            return 0;

        // Code for X < XSMALL
        if (x < XSMALL) {
            clausn = x*(1 - Math.log(x));
        }

        // Code for XSMALL <=  X <=  PI
        else {
            double t = x*x/PISQ - 0.5;
            t += t;
            if (t > 1) t = 1;
            clausn = x*Chebyshev.cheval(NTERMS, ACLAUS, t) - x*Math.log(x);
        }
        if (indx < 0)
            clausn = -clausn;
        return clausn;
    }

}