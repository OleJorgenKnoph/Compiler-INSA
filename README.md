# Computer Systems Project
## Compiler
Design of a simple compiler for a simplified Java language using ANTLR v4.10.1. Part of a project during my semester abroad the spring of 2022 at [INSA Toulouse](https://www.insa-toulouse.fr/fr/index.html), France.

#Testing
##Output of the two testfiles, including sourcecode and parse tree

###TestFile1.txt
```java
public static void main(String[] args){
    int a,b,c = 0;
    b=1;
    c=2;

    int d = (c*c)+(c*b);

   System.out.println(a);
   System.out.println(b);
   System.out.println(c);
   System.out.println(d);
 }
```

###TestFile2.txt
```java
public static void main(String[] args){
    int a_1, b_2, c_3 = 0;

    a_1 = 10.5;
    b_2 = 20;
    c_3 = 15;

    if ( a_1 < b_2){
        System.out.println(c_3);
    }
    else {
        System.out.println(b_2);
    }

    System.out.println(a_1);
}
```
<img width="643" alt="Skjermbilde 2022-06-03 kl  00 39 52" src="https://user-images.githubusercontent.com/70891970/171757026-552d1fde-aa06-4d19-b277-68b30ab4eb72.png">
