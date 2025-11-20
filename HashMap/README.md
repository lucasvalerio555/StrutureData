# StructureData

Method that StructureData utilis in the workflow in the public area.

- add
- remove
- showData
- clear
- get
- size
- isEmpty

__The method "Add"__
  
  
  
=======
# 📦 StructureData

       _____ _             _              _        ____        _       
      / ____| |           | |            | |      |  _ \      | |      
     | (___ | |_ _   _  __| |_   _ _ __  | |_ ___ | |_) | __ _| |_ ___ 
      \___ \| __| | | |/ _` | | | | '_ \ | __/ _ \|  _ < / _` | __/ _ \
      ____) | |_| |_| | (_| | |_| | | | || || (_) | |_) | (_| | ||  __/
     |_____/ \__|\__,_|\__,_|\__,_|_| |_| \__\___/|____/ \__,_|\__\___|

```{=html}
<p align="center">
```
`<img src="https://img.shields.io/badge/Language-Java-blue?style=flat-square" />`{=html}
`<img src="https://img.shields.io/badge/Status-Active-brightgreen?style=flat-square" />`{=html}
`<img src="https://img.shields.io/badge/Version-1.0.0-orange?style=flat-square" />`{=html}
`<img src="https://img.shields.io/badge/License-MIT-lightgrey?style=flat-square" />`{=html}
```{=html}
</p>
```
# 📑 Table of Contents

1.  Project Overview\
2.  Folder Structure\
3.  Implemented Structures\
4.  Unified Method: add()\
5.  Examples\
6.  Visualization\
7.  Coding Standards\
8.  Contributing\
9.  License

# 🚀 Project Overview

**StructureData** is a lightweight and modular collection of classic
data structure implementations written in **pure Java**.\
It focuses on clarity, clean design, extensibility, and educational
value.

# 📂 Folder Structure

    /StructureData
     ├── /list
     │     └── List.java
     ├── /tree
     │     └── Tree.java
     ├── /stack
     │     └── Stack.java
     ├── /queue
     ├── Main.java
     └── README.md

# 📘 Implemented Structures

## List (Linked List)

Methods: - add - remove - showData - get - clear - size - isEmpty

## Tree (Binary Tree)

Methods: - add - insert - insertLeft - insertRight - size

## Stack (LIFO)

Methods: - add (push) - pop - peek - size - isEmpty

## Queue (Coming Soon)

Methods: - enqueue - dequeue - peek - size - isEmpty

## HashMap(Table hash)
 Methods: -push(key , element) - size -isEmpty

# 🧩 Unified Method: add()

  Structure   Behavior
  ----------- ------------------------------
  List        Adds element at end
  Tree        Inserts node following rules
  Stack       Pushes element to top
  Queue       Will enqueue
  HashMap

# 🧪 Examples

## Basic Usage

    List<String> list = new List<>();
    list.add("Apple");
    list.add("Banana");
    list.showData();

    Tree<Integer> tree = new Tree<>();
    tree.add(10);
    tree.insertLeft(10, 5);

    Stack<String> stack = new Stack<>();
    stack.add("A");
    stack.add("B");
    System.out.println(stack.pop());

    Map<Integer, String> hash =new HashMap<>();
    hash.push(1,"Lucas");
    hash.push(2,"Juan");
    hash.push(3,"Pedro");

# Visualization

## Linked List

    [HEAD] → | A | → | B | → | C | → null

## Binary Tree

           (10)
          /    \
        (5)    (20)
               /
             (15)

## Stack

     TOP
     ┌─────┐
     |  A  |
     ├─────┤
     |  B  |
     ├─────┤
     |  C  |
     └─────┘
    BOTTOM


## HashMap
     ┌──────────┌──────────┐
     |   key    |  Value   |
     ├─────────────────────┤
     |    1     |  "Lucas" |
     ├──────────├──────────┤
     |    2     |  "Juan"  |
     └─────────────────────┤
     |    3     |  "Pedro" |
     └─────────────────────┘

# Coding Standards

-   CamelCase
-   Generics
-   No external dependencies
-   Consistent API
-   Clean documentation

# Contributing

Contributions are welcome: - New structures - Optimizations -
Documentation - Unit tests

# License

MIT License.

Copyright (c) 2025 StructureData

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
>>>>>>> bedd57b (Versión final)
