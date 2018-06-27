# This repository has been moved to [gitlab.com/paul-nechifor/magic-square](http://gitlab.com/paul-nechifor/magic-square).

Old readme:

# Magic Square

The is a Java program that searches for [magic squares][msq].

![magic square cover](screenshot.png)

You can [read more about it][pm] on my page (in Romanian). Sorry about the bad
code, I wrote it a while ago.

## Run it

Build it:

    mvn package

Run the Swing GUI:

    java -cp target/magic-square-*.jar net.nechifor.magic_square.Main

Try it from the console:

    java -cp target/magic-square-*.jar net.nechifor.magic_square.Console BIHC 16

## License

MIT

[msq]: http://en.wikipedia.org/wiki/Magic_square
[pm]: http://old.nechifor.net/patrat-magic
