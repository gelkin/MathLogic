#include <cstdio>

typedef long long Number;

struct Z {
    template <typename... Args>
    static Number call(Args... xs) {
        return 0;
    }
};

struct N {
    static Number call(Number x) {
        return x + 1;
    }
};

template <int i>
struct U {
    template<typename... Args>
    static Number call(Number a, Args... args) {
        return U<i - 1>::call(args...);
    }
};

template<>
struct U<0> {
    template <typename... Args>
    static Number call(Number a, Args... args) {
        return a;
    }
};

template <typename f, typename... g>
struct S {
    template <typename... Args>
    static Number call(Args... args) {
        return f::call(g::call(args...)...);
    }
};

// todo change places of arguments
template <typename f, typename g>
struct R {
    template <typename... Args>
    static Number call(Number y, Args... xs) {
        if (y > 0) {
            return g::call(y - 1, xs..., R<f, g>::call(y - 1, xs...));
        } else {
            return f::call(xs...);
        }
    }
};

// change loop name
template <typename f>
struct M {
    template <typename... Args>
    static Number call(Args... args) {
        int i = 0;
        while (true) {
            if (f::call(args..., i) == 0) {
                return i;
            }
            i++;
        }
    }
};

// plus:
typedef R< U<0>, S< N, U<2> > > plus;

// minus:
typedef S< R<Z, U<0> >, U<0>, U<0> > minus1;
typedef R<U<0>, S< minus1, U<2> > > original_minus;
typedef S< original_minus, U<1>, U<0> > minus; // original_minus with swapped args

// mul:
typedef R< Z, S< plus, U<1>, U<2> > > mul;

// div:
typedef S< minus, S < N, U<0> >,  S <mul, U<1>, U<2> > > help_div; // a' - (b * x)
typedef S < minus1, M< help_div > > div;

// mod:
typedef S <minus, U<0>, S <mul, U<1>, S < div, U<0>, U<1> > > > mod;

// is_prime:
typedef S < Z, U<0> > zero; // zero from multiple args
typedef S < N, zero > one;
typedef S < N, one > two;
typedef S < R < zero, one >, minus1, U<0> > is_more_1;
typedef S < N, N > plus_2;
typedef S < plus_2, M < S < mod, U<0>, S < plus_2, U<1> > > > > min_divdr_grtr_1;
typedef S < N, S < div, U<0>, two > > help_is_prime; // (x/2) + 1
typedef S <
            R <
                zero,
                S <
                    R < one, zero >,
                    S < S < minus, help_is_prime, min_divdr_grtr_1 >, U<1> >,
                    U<1>
                  >
              >,
            is_more_1,
            U<0>
          >
          is_prime;

// nth_prime
typedef S < R <one, zero>, S < is_prime, S < plus, U<0>, U<1> > >, U<0> > is_sum_not_prime;
typedef S < plus, N, S < M < is_sum_not_prime >, N > > next_prime;
typedef S < R < two, S < next_prime, U<2> > >, U<0>, U<0> > nth_prime;

// pow
typedef S < R< one, S< mul, U<1>, U<2> > >, U<1>, U<0> > pow;

// real_log
typedef S < R < one, zero >, S < minus, S < pow, U<0>, U<2> >, U<1> >, U<0> >  help_real_log; // help_real_log(p, x, y) = ((p^y - x) > 0)?
typedef S < minus1, M < help_real_log > > real_log;

// plog
// todo is_not_zero(x)
// todo
typedef S < R<zero, one>, U<0>, U<0> > is_not_zero;
typedef S< mod, U<1>, S< pow, U<2>, U<0> > > help_mod; // help_mod(r, x, p) = x % p^r
// (r, x, p, plog(r, x, p)) = (x % p^r == 0)? r: plog(r, x, p)
typedef S< R< U<0>, U<2> >, S< is_not_zero, S< mod, U<1>, S< pow, U<2>, U<0> > > >, U<0>, U<3> >  help_plog;
typedef S < R< help_plog, zero >, S < N, real_log > > plog; // have to add 1 to real_log(x)
