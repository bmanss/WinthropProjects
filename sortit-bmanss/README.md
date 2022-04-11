[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-f059dc9a6f8d3a56e377f745f24479a46679e63a5d9fe6f495e02850cd0d8118.svg)](https://classroom.github.com/online_ide?assignment_repo_id=7531540&assignment_repo_type=AssignmentRepo)
# SortIt

### Overview

Your goal in this assignment is to write a program that combines and sorts input files. It will do this by reading in lines from one or more input files, sorting them using a non-recursive version of mergesort, and then printing the results.

In Java, your main source should be in a file named **sortit.java**, and your main method should be in a class named **sortit**. (Note the lowercase 's'.) Your program should run from the command line in your submission directory by invoking
```
java sortit <cmd-line-args>
```

### Specifications


#### Running the program (command line)

This program will take file names on the command line and read the contents of each file in line by line. If a "-" (i.e., a hyphen) is specified for a file name, the program should use _standard input_ for that file rather than opening a file called "-". For example,
```
sortit file1.txt - file3.txt
```
will sort all of the lines in **file1.txt**, standard input, and **file3.txt**. Note that the order of files on the command line is unimportant because you'll be sorting all of the lines together; it doesn't matter whether you read the lines from **file1.txt** or **file3.txt** first.

The sorted lines should be printed to _standard output_; for testing, you may redirect standard output to a file.


#### Program operation

Your program will combine the lines from each of the files specified on the command line, and produce (on standard output) the lines from all of the files sorted lexicographically (in other words, sorted using the Java String `compareTo()` method).

For the contents of each file specified on the command line, read each line into memory and add it to one of two linked lists (**list0** and **list1**). Initially, you'll add elements to the tail of **list0**; however, when you see a line that's _less_ than the previous line, switch lists. This will continue through the entire process, so you should be frequently switching between **list0** and **list1** and back.

Once you've read in the contents of all of the files, you're ready to sort the lines. This version of mergesort is non-recursive, and uses two linked lists to build up longer and longer _runs_ of sorted lines. A run of sorted lines is a sequence of one or more lines that are sorted in _increasing_ (actually, _non-decreasing_) order; as soon as you encounter a line "less than" the previous line, a new run starts. An example using integers rather than strings is:
```
5 12 8 15 9 7 4 11 25 30
```
This sequence of integers consists of the runs [5,12], [8,15], [9], [7], and [4,11,25,30]. Note that two of the runs ([9] and [7]) consist of just one element. If these numbers were placed according to the algorithm described above, **list0** and **list1** would look like this:
```
list0: 5 12 9 4 11 25 30
list1: 8 15 7
```
(Again, the above example uses integers, just for simplicity and compactness. Your program will be sorting strings, not integers.)

To sort the lists, build longer and longer runs by combining the runs at the heads of **list0** and **list1**, and placing the resulting run at the back of a single list. You'll make a series of passes through both lists. You'll examine the head of each list. If you can continue the run with _both_ elements, choose the smaller, as that will allow you to create the longer run. If you can continue the run with only one element, do so by removing that element and placing it at the end of the "current destination" list (either **list0** or **list1**). If you cannot continue the run, i.e., both elements at the head of the two lists are smaller than the last element you encountered, then switch your destination list and remove the smaller element and place it at the end of the (just-switched-to) destination list. Continue this procedure until you've processed all the elements of your original two lists. (As you're adding to the end of your lists, you'll need some way to keep track of this.) Continue doing these passes until all the lines are sorted; this occurs when all the elements are on one list at the end of a pass. The algorithm may end up with all lines in either **list0** or **list1**; it's not always going to be the same list.

For this project I am not going to supply you with pseudocode for the algorithm. You'll need to understand the process, and come up with the details of the algorithm on your own. I encourage you to post your questions, design, pseudocode on Zulip. I will provide feedback for you (and others) to see. You can post short code fragments if you have a general question.

I cannot emphasize enough the importance of understanding what you're doing before you start coding. You should do this with paper and pencil, and a few numbers or short strings. Write some pseudocode, then test it out (on paper) with a variety of inputs. When you ask me for help I will likely ask you for your pseudocode.

After the lines are sorted, they should be printed to standard output in sorted order (the order in which they're on the list). Note that your program should print nothing else to standard output other than the lines that were read in. If there were a total of 17 lines of input, you should print exactly 17 lines to standard output. If your program is given no input, it should print nothing.

The program you are writing is equivalent to a simple version of the Unix command **sort** (without all the command line options). You can see what your output should look like by running this command on a Unix or Linux system. The Windows Command prompt also has a **sort** command that works similarly.


#### Error handling

You're only sorting lines of a file, so there aren't many errors to handle. However, you do need to handle one error: file not found. If a file can't be opened, it should be skipped and an error message printed on _standard error_. An empty file is not an error; it simply doesn't contribute any lines to be sorted.


#### Program design and implementation

This program only needs a singly-linked list data structure that can store strings of any length. I want you to build your own linked list data structure, because I want you to see how simple this is to do. You do not need a general linked list class, because you're only removing elements from the head of the list, and appending them to the end of the list. This is essentially a queue, implemented as a linked list.

Make sure you can remove the first element and append to the end of the list in constant time. You should _not_ traverse the entire list to add an element to the end. In other words, the _enqueue_ operation should be implemented efficiently. Consult your data structures text, or ask, if you don't understand how to do this.

When you finish using a file, you should close it to free up resources used by the system.


### Hints



* **Start early!**
* Trace through the algorithm on paper to get a feel of how it works.
* Do your design document first. If you need help with it, please ask for help during the _first_ week the assignment is out. Don't wait until the day before the assignment is due to start.
* Write your code for singly-linked lists first. Test the code by reading a file into a linked list and then printing it out again without sorting it. I expect you to write your own linked list code, and not use someone else's, or Java's LinkedList class, or mindlessly copy the code from Shaffer. You may use the code from Shaffer as a reference. My rationale: I want you to see how simple it is to implement a basic linked-list queue from scratch.
* Use plenty of debugging code. When you're debugging, you may want to send messages to standard error.


### Extra Challenge (this entire section is optional)

For those who want a greater challenge, you can implement the following options from the Unix sort command:



* -r: sorts lines in reverse order (from lexicographically greatest to least)
* -u: eliminates duplicate lines. Lines are duplicates if they are _exactly_ identical (whitespace and all). Rather than printing multiple copies of a line that occurs multiple times, only one copy of the line is printed.
* -f: fold lower-case characters into the corresponding upper-case characters. For example, 'b' would be converted to 'B' for purposes of sorting. Note that lines should be printed using the _original_ case, so lines may contain both upper and lower case letters.

Unlike the Unix program, you don't need to deal with multiple options bunched together; if someone wants to sort in reverse order folding lower case letters into upper case, they need to type **sortit -r -f** or **sortit -f -r**. You don't need to recognize **sortit -fr**. For example
```
sortit -r -f file1.txt file2.txt
```
would enable reverse sorting and case folding for _both_ **file1.txt** and **file2.txt**.

Don't attempt the extra challenge until your **sortit** program works perfectly. If you implement more than one of the options, they should all work together (as well as separately).
