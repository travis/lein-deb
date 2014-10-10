# lein-deb

A leiningen plugin for building deb packages.

lein-deb relies on [ant-deb-task](https://code.google.com/p/ant-deb-task/wiki/deb) for building debs.

{:toc}

## Usage

Add a :deb section to your project.clj as shown in sample.project.clj

~~~
    > lein do clean, deb
~~~


## References

   - [The Ant deb task](https://code.google.com/p/ant-deb-task/wiki/deb)
   - [Ant TarFileSet](https://ant.apache.org/manual/Types/tarfileset.html)
   - [Ant FileSet](https://ant.apache.org/manual/Types/fileset.html)
   - [Debian package basics](https://www.debian.org/doc/manuals/debian-faq/ch-pkg_basics.en.html)

## License

Copyright (C) 2010 Travis Vachon

Distributed under the Eclipse Public License, the same as Clojure.
