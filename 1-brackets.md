# Prompt

Write a function that determines whether an input string has balanced brackets.

# Additional explanation

You are given an input string consisting of brackets—square `[ ]`, round `( )`, and curly `{ }`. The input string can include other text. Write a function that returns either `true` if the brackets in the input string are balanced or `false` if they are not. Balanced means that any opening bracket of a particular type must also have a closing bracket of the same type.

An empty input string or a string without brackets can also be considered "balanced".

# Examples

```java
Brackets.check("[][(){}"); // false
Brackets.check("({)}"); // false
Brackets.check("({[]})"); // true
Brackets.check("text ( is allowed ){rwwrwrrww [] ()}"); // true
```

As a bonus, return a value that tells the caller what bracket was
mismatched.

# Solutions

In general an optimal approach is to keep a *stack* of open brackets, pushing as we come across them. As we come across closing brackets, if they match the most-recently-opened bracket we can pop the most-recently-opened bracket. If our stack is empty once we reach the end of the string, then our brackets must have been balanced.

Solution [here](src/barnard/Brackets.java).