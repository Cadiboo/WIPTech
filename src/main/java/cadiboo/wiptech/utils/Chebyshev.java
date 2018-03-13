package cadiboo.wiptech.utils;

/**
 * Evaluate Chebyshev series.
 * <p>
 * This class evaluates a Chebyshev series, using the
 * Clenshaw method with Reinsch modification, as analysed
 * in the paper by Oliver.
 * <p>
 * This is a Java port of code originally written in Fortran.
 * The Fortran code this is based upon was last modified 21 December, 1992.
 *
 * @author <a href="mailto:macl_ms0@paisley.ac.uk">Dr. Allan J. MacLeod</a>
 *         (original Fortran implementation)
 * @author <a href="mailto:Martin.vGagern@gmx.net">Martin von Gagern</a>
 *         (Java port)
 *
 * @see <a href="http://dx.doi.org/10.1145/232826.232846">MISCFUN</a>
 * @see <a href="http://dx.doi.org/10.1093/imamat/20.3.379">Error Analysis</a>
 */
public class Chebyshev {

    /**
     * Constant used to decide the use of the Reinsch modification
     */
    private static double TEST = 0.6;

    /**
     * Evaluate a Chebyshev series.
     *
     * @param n the number of terms in the sequence
     * @param a the coefficients of the Chebyshev series
     * @param t the value at which the series is to be evaluated
     */
    public static double cheval(int n, double[] a, double t) {
        double u1 = 0, u2 = Double.NaN;

        // If ABS ( T )  < 0.6 use the standard Clenshaw method
        if (Math.abs(t) < TEST) {
            double u0 = 0;
            double tt = t + t;
            for (int i = n; i >= 0; --i) {
                u2 = u1;
                u1 = u0;
                u0 = tt*u1 + a[i] - u2;
            }
            return (u0 - u2)/2;
        }

        // If ABS ( T )  > =  0.6 use the Reinsch modification
        else {
            double d1 = 0, d2 = Double.NaN;

            // T > =  0.6 code
            if (t > 0) {
                double tt = (t - 0.5) - 0.5;
                tt += tt;
                for (int i = n; i >= 0; --i) {
                    d2 = d1;
                    u2 = u1;
                    d1 = tt*u2 + a[i] + d2;
                    u1 = d1 + u2;
                }
                return (d1 + d2)/2;
            }

            // T < =  -0.6 code
            else {
                double tt = (t + 0.5) + 0.5;
                tt += tt;
                for (int i = n; i >= 0; --i) {
                    d2 = d1;
                    u2 = u1;
                    d1 = tt*u2 + a[i] - d2;
                    u1 = d1 - u2;
                }
                return (d1 - d2)/2;
            }
        }
    }

}