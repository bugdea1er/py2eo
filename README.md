
<img src="https://www.yegor256.com/images/books/elegant-objects/cactus.svg" height="100px" />

[![Java CI](https://github.com/polystat/py2eo/actions/workflows/github-ci.yml/badge.svg)](https://github.com/polystat/py2eo/actions/workflows/github-ci.yml)

This is a translator of Python to [EOLANG](https://www.eolang.org).

User Quick Start
----
1. How to transpile (your own) Python (test) to EO
Environment:
* linux, windows (todo: versions?)
* [Java 11+](https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_windows-x64_bin.zip) - check in command line `java --version`

Run info:
1. ? get jar from release https://github.com/polystat/py2eo/releases
2. download [archive](https://s01.oss.sonatype.org/service/local/repositories/releases/content/org/polystat/py2eo/0.0.2/py2eo-0.0.2.jar) with executable file / or open [link](https://s01.oss.sonatype.org/#nexus-search;quick~py2eo) and download `py2eo-0.0.2.jar`
3. Put yor python code to .py next to jar
4. run `java -jar path_to_jar path_to_py_code`
5. check output .eo file in `path_of_py/genCageEO/name_of_py.eo`

-----
Sample .py code:
```
def conditionalCheck2():
    a = 4
    b = 2

    return a > b
```
-----
Sample .eo output from the code scope above in the `path_of_py/genCageEO/name_of_py.eo`:
```
+package org.eolang
+alias goto org.eolang.gray.goto
+alias stdout org.eolang.io.stdout
+alias cage org.eolang.gray.cage
+junit

[unused] > test1
  cage > result
  cage > tmp
  [unused] > conditionalCheck1
    cage > result
    cage > tmp
    cage > a
    cage > e0
    cage > b
    goto > @
      [returnLabel]
        seq > @
          stdout "conditionalCheck1\n"
          (a).write (1)
          (b).write (2)
          (e0).write (((a).less (b)).@)
          [] > tmp1
            e0' > copy
            copy.< > @
          (result).write (tmp1.copy)
          returnLabel.forward 0
          123
  goto > @
    [returnLabel]
      seq > @
        stdout "test1\n"
        tmp.write (((conditionalCheck1)) 0)
        (tmp.@)
        (result).write (tmp.result)
        xresult
```
