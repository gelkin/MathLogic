#include <cstdio>
#include <vector>

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
        return U<i-1>::call(args...);
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

template <typename f, typename g>
struct R {
    template <typename... Args>
    static Number call(Number y, Args... xs) {
        Number result = f::call(xs...);
        for (Number x = 0; x < y; ++x) {
            result = g::call(x, xs..., result);
        }
        return result;
    }
};

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

typedef S < Z, U<0> > zero; // zero from multiple args
typedef S < N, zero > one;
typedef S < N, one > two;

struct dec {
    static Number call(Number x) {
        return x - 1;
    }
};

struct negation {
    static Number call(Number x) {
        return !x;
    }
};

struct add {
    static Number call(Number x, Number y) {
        return x + y;
    }
};

struct subtract {
    static Number call(Number x, Number y) {
        return x - y;
    }
};

struct div {
    static Number call(Number x, Number y) {
        return x / y;
    }
};

struct mod {
    static Number call(Number x, Number y) {
        return x % y;
    }
};

struct mul {
    static Number call(Number x, Number y) {
        return x * y;
    }
};

struct pow {
    static Number call(Number x, Number y) {
        Number result = 1;
        while (y > 0) {
        	result *= x;
        	y--;
        }
        return result;
    }
};

struct plog {
    static Number call(Number x, Number y) {
        Number result = 0;
        while ((y % pow::call(x, result + 1)) == 0) {
            result++;
        }
        return result;
    }
};

struct nthPrime {
    static std::vector<Number> primes;

    static Number call(Number n) {
        for (Number x = primes.back() + 1; primes.size() <= n; x++) {
            bool is_prime = true;
            for (Number prime : primes) {
                if (!(is_prime = x % prime)) {
                    break;
                }
            }
            if (is_prime) {
                primes.push_back(x);
            }
        }
        return primes[n];
    }
};

std::vector<Number> nthPrime::primes { 2 };

// Number of min prime number, which is not divider of U<0>

typedef M< S< negation, S< mod, U<0>, S< nthPrime, U<1> > > > > minPrimeGt;
// push(stack, number) = stack * nthPrime(minPrimeGt(stack))^(number + 1)
typedef S< mul, S< pow, S< nthPrime, S< minPrimeGt, U<0> > >, S< N, U<1> > >, U<0> > push;

// help_pop(x, y) = x / y^(plog(y, x))
typedef S< div, U<0>, S< pow, U<1>, S< plog, U<1>, U<0> > > > help_pop;

// pop(stack) = help_pop(stack, nthPrime(minPrimeGt(stack) - 1))
typedef S< help_pop, U<0>, S< nthPrime, S< dec, minPrimeGt > > > pop;

// peek(x) = plog(nthPrime(minPrimeGt(x) - 1), x) - 1
typedef S< dec, S< plog, S< nthPrime, S< dec, minPrimeGt > >, U<0> > > peek;

typedef S< negation, dec > is_empty;
// Exemines if stack == 2 ^ n
typedef S< negation, S<dec, minPrimeGt> > is_size_one;

// peek_second(stack) = peek(pop(stack))
typedef S< R< pop, S< peek, U<2> > >, one, U<0> > peek_second;

typedef S< R< pop, S< pop, U<2> > >, one, U<0> > pop2;
typedef S< R< S< pop2, U<0> >, S< push, U<3>, U<2> > >, one, U<0>, U<1> > pop2_push;
typedef S< R< S< pop2_push, U<0>, U<1> >, S< push, U<4>, U<3> > >, one, U<0>, U<1>, U<2> > pop2_push2;
typedef S< R< S< pop2_push2, U<0>, U<1>, U<2> >, S< push, U<5>, U<4> > >, one, U<0>, U<1>, U<2>, U<3> > pop2_push3;

typedef S< pop2_push, U<0>, S< N, peek > > akk_0_n;
typedef S< pop2_push2, U<0>, S< dec, peek_second >, one > akk_m_0;
typedef S< pop2_push3, U<0>, S< dec, peek_second >, peek_second, S< dec, peek > > akk_m_n;

// Implementation of Akkerman function definition.
// akk_switch(m, n) =
// 					akk_0_n, when m == 0
// 					akk_m_0, when n == 0
// 					akk_m_n, otherwise
typedef S<
            R<
                S<
                    R< akk_m_n, S< akk_m_0, U<1> > >,
                    S< negation, peek >,
                    U<0>
                >,
                S<
                    akk_0_n,
                    U<1>
                >
            >,
            S< negation, peek_second >,
            U<0>
        >
        akk_switch;

// Applies akk_switch to U<0>, U<1> times.
typedef S< R< U<0>, S< akk_switch, U<2> > >, U<1>, U<0> > apply_akk_switch;

// We try to find such number 'x', which is number of correct applying akk_switch
// to stack to get one Number.
typedef M< S< negation, S< is_size_one, apply_akk_switch > > > switch_number;

// First initialization of stack
typedef S< push, S< push, one, U<0> >, U<1> > init_stack;

// We apply akk_switch 'switch_number' times and because of 'switch_number's construction
// obtain stack consisting of 1 Number and peek it.
// akk(x, y) = peek( apply_akk_switch( stack(x, y), switch_number(stack(x, y)) ))
typedef S< S< peek, S< apply_akk_switch, U<0>, switch_number > >, init_stack > akk;

int main() {
    int m = 2;
    int n = 2;
    printf("%lld\n", akk::call(m, n));
}

