# Simple Lexer
This is a little simply expandable lexer written fully in Java.<br>
Heavily inspired by [Noq](https://github.com/tsoding/Noq) lexer.

## Docs (kind of)
### Lexer interface
| Method                                     | Description                                                                                                                                                                   |
|--------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| new Lexer(String source, String path)      | Creates a lexer. The first argument is the source string itself and second is path to file (needed to provide location of the token). Automatically parses the source string. |
| void setSource(String source, String path) | The first argument is the source string itself and second is path to file (needed to provide location of the token). Parses the string.                                       |
| boolean nextToken()                        | Increases token counter. Returns `true` if the token with such index is present. Returns `false` if such token is absent.                                                     |
| boolean prevToken()                        | Decreases token counter. Returns `true` if the token with such index is present. Returns `false` if such token is absent.                                                     |
| Token token()                              | Returns Token object which is currently selected.                                                                                                                             |
 | List\<Token> getTokens()                   | Returns ArrayList of Tokens.                                                                                                                                                  |

### Token interface

| Method                                        | Description                                                                                                                                                                                             |
|-----------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Location getLoc()                             | Returns object of type `Location` which represents the place of a token in a source string.                                                                                                             |
| void setLoc(Location loc)                     | Sets the location of a token.                                                                                                                                                                           | 

### Token inheritors 
| Name             | Description                                                 |
|------------------|-------------------------------------------------------------|
| Token.Identifier | Holds the String which contains the name of the identifier. |
| Token.Keyword    | Holds the enum which contains the keyword type.             |
| Token.Special    | Holds the enum which contains the special character type.   |

P.S. Maybe it makes sense to unite `Token.Keyword` and `Token.Special` in the context of this particular lexer, as they are practically identical.

### Token.Location interface
| Field       | Accessible with    | Description                                               |
|-------------|--------------------|-----------------------------------------------------------|
| String path | \<location>.path() | Holds the path to the file that provides this token.      |
| long col    | \<location>.col()  | Holds the number of a column where this token is located. |
| long row    | \<location>.row()  | Holds the number of a row where this token is locared.    |                                      
