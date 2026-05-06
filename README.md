# RSA-Exchange

Compile the project with "javac RSA.java Alice.java Bob.java". Then, open two terminals (we usually used the "Split Terminal" option in VSCode). On one terminal, run "java Bob" to generate RSA keys and display all values, including the necessary values n and e.

Next, on the other terminal, run "java Alice", input Bob’s n and e, and enter a message you want to send to Bob. Alice will output an encrypted ciphertext.

Then, paste the ciphertext back into Bob’s program to decrypt it and confirm that the original message is recovered.