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
