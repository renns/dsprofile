The dsprofile library
=====================

The dsprofile library provides general facilities to implement domain-specific
profiling in Scala programs.

The design of the library and an application to attribute grammar profiling
are described in "Profile-based Abstraction and Analysis of Attribute
Grammars" by Anthony M. Sloane, Proceedings of the International Conference on
Software Language Engineering, 2012.

Building the library
====================

Currently the library is not distributed via any repositories and we do not
upload pre-built jars.

So, if you want to use it, you will need to build it. First, clone this
repository using Mercurial.

Then, download and install the Scala simple build tool:

    https://github.com/harrah/xsbt/wiki/

We have tested the build with sbt 0.12.0.

Finally, run `sbt package` in the top-level of the project. sbt will download
all the necessary Scala compiler and library jars, build the library, and
package it as a jar file.

If all goes well, you should find the dsprofile library jar in the `target`
directory under a sub-directory for the Scala version that is being used.
E.g., if the Scala version is 2.10, look in `target/scala_2.10` for
`dsprofile_2.10-VERSION.jar` where `VERSION` is the dsprofile library version.

Using the library from Scala
============================

The dsprofile library operates in terms of abstract events that represent
various occurrences as a program executes. For example, suppose that we are
interested in profiling the evaluation of attributes in a running attribute
grammar evaluator. The occurrences in this case are evaluations of
attributes.

We need a way to pass information from the evaluator execution and a way to
trigger profiling reports to be printed.

Information is passed to the profiler by calling the `start` and `finish`
methods of the `Events` module. The parameters to these messages are Scala
tuples that describe the event that has occurred. For example, to indicate
that an attribute evaluation has started, your code might call:

    start ("event" -> "AttrEval", "subject" -> s, "attribute" -> a,
           "parameter" -> None)

Each of the strings `"type"`, `"subject"` and so on is a _dimension_ and
together they identify the particular event that has occurred. The second
component of each tuple is an arbitrary value for the corresponding dimension
in this particular occurrence. For example, the value `s` is assumed in the
example to be a reference to the subject node of the evaluation; i.e., the
syntax tree node whose attribute is being evaluated. Similarly, `a` is a value
that refers to the attribute that is being evaluated.

Both the dimensions and their values are arbitrary as far as the library is
concerned. The dimensions are just strings and it is up to the user to
establish a convention for their meaning. Similarly, the dimension values are
arbitrary objects as far as the profiling library is concerned. However, it is
best to make sure that they have a sensible `toString` implementation, since
that method is used by the library to print the values (see below).

At the point when an interesting execution region has completed, the
program code should call the `Events.finish` method. This method has
two parameter lists. The first one should be given the same information
as the corresponding `start` event. The library uses this list to 
make sure that the `start` and `finish` attributes are properly nested.
The second parameter list can contain other dimensions and values that
are only known once the evaluation has finished.

For example, in the attribute evaluation case, we might call `finish`
as follows.

    finish ("event" -> "AttrEval", "subject" -> s, "attribute" -> a,
            "parameter" -> None) ("value" -> v, "cached" -> false)

We can see that the first parameter list is the same in the `start` call. In
addition, the new `"value"` and `"cached"` dimensions are given values here,
because we only know what they are once the attribute occurrence has been
fully evaluated. As before, the value `v` is an arbitrary object that is the
attribute value. The `cached` dimension is a Boolean that indicates whether
the value of the attribute was obtained from its cache or not.

The other main entry point for the library is the `Profiler.profile` method.
It should be called with the first argument being the computation that you
want to profile. This argument is passed by name to the `profile` method so it
will not be evaluated until necessary. The computation should call the `start`
and `finish` methods as described above, but it can do any other computation
as well. The second argument to `profile` is a list of the dimensions that you
want to see in the profile report. For example, we might call

    profile (c, List ("attribute", "cached"))

to profile the evaluation of `c` and print a report along two dimensions,
first the attribute that was evaluated and then whether it was cached or not.

The first part of a report produced by this call is:

       716 ms total time
        95 ms profiled time (13.3%)
      2543 profile records
    
    By attribute:
    
     Total Total  Self  Self  Desc  Desc Count Count
        ms     %    ms     %    ms     %           %
        56  59.4    18  19.7    37  39.7   621  24.4  entity
        43  45.4    28  29.4    15  16.0   691  27.2  env.in
        30  32.4     7   7.5    23  24.9   203   8.0  tipe
        27  29.2    15  15.8    12  13.4   293  11.5  env.out
        25  26.7     7   8.4    17  18.4    94   3.7  idntype
        23  24.8     3   4.2    19  20.6    97   3.8  basetype
         5   5.3     4   5.0     0   0.3    81   3.2  exptype
         3   3.7     3   3.7     0   0.0    81   3.2  rootconstexp
         3   3.5     3   3.5     0   0.0   234   9.2  typebasetype
         1   2.1     1   1.9     0   0.2    96   3.8  deftype
         0   1.0     0   1.0     0   0.0    52   2.0  level
    
    By cached for entity:
    
     Total Total  Self  Self  Desc  Desc Count Count
        ms     %    ms     %    ms     %           %
        14  14.7    12  12.8     1   1.9   158   6.2  false
         6   6.8     6   6.8     0   0.0   463  18.2  true
    
    By cached for env.in:
    
     Total Total  Self  Self  Desc  Desc Count Count
        ms     %    ms     %    ms     %           %
        27  28.9    25  27.1     1   1.8   536  21.1  false
         2   2.2     2   2.2     0   0.0   155   6.1  true

The profile report first gives a summary of the evaluation containing the
total time that the execution took, how much of it was accounted for by
profiled evaluations and how many profile records were created. In this case,
since we are just profiling attribute evaluations, the number of profile
records is equal to the number of attribute occurrences that were evaluated.

Following the summary, we get a table showing the profile records bucketed by
the first dimension. The columns show the total time taken to evaluate each
attribute, the time accounted for by the  attribute itself, the time accounted
for by evaluation of other attributes needed by the attribute being
summarised, and the count of how many evaluations were performed.

After the first dimension table, we get one table for each row  in the first
dimension table summarising the second dimension.

In theory the number of dimensions in a report is not limited, but it gets
quite hard to understand after about three or four dimensions.

The last string printed on each line of a table is the `toString` string of
the relevant dimension value. The library will try to detect when this string
will not fit on the line. In those cases, it will print a footnote number
instead and will print the value as a footnote after all of the tables have
been printed. Thus, the dimension could be something such as a tree node which
might be printed in full or pretty-printed.

Using the library from Java
===========================

In theory Java code should be able to call into the Scala code. We will
document this pathway more in future once we have more experience with it. We
expect to use that experience to provide a more convenient interface for Java.
