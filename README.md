# Compiler
Design of a simple compiler for a simplified Java language using ANTLR v4.10.1. Part of a project during my semester abroad the spring of 2022 at [INSA Toulouse](https://www.insa-toulouse.fr/fr/index.html), France.

## Testing
##Output of the two testfiles, including sourcecode and parse tree

### TestFile1.txt
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
#### Output
Output alongside given assembly code
<img width="581" alt="Skjermbilde 2022-06-03 kl  01 41 56" src="https://user-images.githubusercontent.com/70891970/171758803-30d022ce-0d77-4cc9-8c9d-88d0662dd639.png">

#### ParseTree
ParseTree generated after running main

![parseTree1](https://user-images.githubusercontent.com/70891970/171758961-1521cb79-4b26-4c7c-ba14-8fda44a33c08.png)



### TestFile2.txt
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
#### Output
Output alongside given assembly code

**Output 1 - Output with code equal to displayed over**

<img width="481" alt="Skjermbilde 2022-06-03 kl  01 52 08" src="https://user-images.githubusercontent.com/70891970/171759028-5fa8135f-4eef-49e3-92de-eccdd9a04f0e.png">

**Output 2 - Output when '<' is switched in code to ensure if/else works as supposed**
`if ( a_1 < b_2) `
> okey
**Output 2 - Output when '<' is switched in code to ensure if/else works as supposed**

<img width="434" alt="Skjermbilde 2022-06-03 kl  01 52 23" src="https://user-images.githubusercontent.com/70891970/171759160-4fcd3bfa-3a11-4938-8b88-e6c578dd7ecc.png">


#### ParseTree
ParseTree generated after running main

![parseTree2](https://user-images.githubusercontent.com/70891970/171759184-241b58e7-5013-4a9b-abac-6008f9e2bdaa.png)

