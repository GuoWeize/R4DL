RUReReAD - Rules-based User-defined Requirements' Relationships Automatic Detector
--------------------------

### Author: 郭维泽 (Guo Weize)

RUReReAD can automatically detect all relationships between requirements according to rules that user defined.
RUReReAD provides an easy-learning formal language to enable user to write their own definitions of various kinds of requirements, and their detection rules of relationships.
RUReReAD can traversal all requirements to detect relationships automatically with a nice performance.

## To start using RUReReAD

User should use it by 4 steps:
 * Input definitions of requirements' models in file [model.txt](src/main/resources/models/model.txt). Specific syntax rules are shown below.
 * Input definitions of relationships recognition rules in file [rule.txt](src/main/resources/definitionFile/rule.txt). Specific syntax rules are shown below.
 * Input requirements to be detected in file [requirement.json](src/main/resources/requirementFile/requirement.json).
 * Run main function in file [api.Main.java](src/main/java/process/Demo.java).

## To start writing definitions
The syntax of language is familiar to other programming language, there can be any number of space or newline between tokens.
However, there should be no space in an identifier.

All identifiers should be consisted of 26 letters (both upper and lower) and `_`, and can not use these keywords:  
boolean, integer, float, string, list, set, map, type, requirement, rule, function,
size_of, is_null, and, or, not, all, any, max, min, synonym, antonym, include, merge, from, to, find, substring

## How to write requirements' models

A requirement model is consisted of all semantic elements, including their names and types.
Users can define requirement models, like:
```text
requirement <requirement_name> {
    <element_name_1> <element_type_1> [= <default_value>] ;
    <element_name_2> <element_type_2> [= <default_value>] ;
    ...
}
```
The `<requirement_name>` and `<element_name>` are identifiers.<br>
The `<default_value>` is a value of type `<element_type>`, which represent the default value of this field.
For basic types, it fit [`this format`](#how-to-write-requirements).
For customized types, it should be the ID of the entity. <br>
The `<element_type>` is a type signature, which has 3 conditions:
 * basic types:
   - `boolean`: `true` or `false`, the default is `false`;
   - `integer`: range from -2147483648 to 2147483647, the default is 0;
   - `float`: double float value, the default is 0.0;
   - `string`: any string like Java String, the default is empty string `""`;
 * collection types: `list` `set` `map`, the default is empty
   - `list`: an array of elements in a same type with a specific order, for example `list<string>`
   - `set`: a set of elements in a same type without any order, for example `set<integer>`
   - `map`: a reflection relationship from one type to another, also without any order, for example `map<integer, string>`
 * user-defined types: a type defined by users, using syntax like:
      ```text
      type <type_name> {
          <field_name_1> <field_type_1> ;
          <field_name_2> <field_type_2> ;
          ...
      }
      ```
   The default is `null`.<br>
**Notice**: user-defined types should be defined before used.

The EBNF formats are:

| Nonterminal Character        | Definition |
|:----------------------------:|:----------:|
| `<requirements_definitions>` | **{ {** `<requirement_definition>` **}<sup>+</sup> {** `<type_definition>` **} }**
| `<requirement_definition>`   | **requirement** `<definition>`
| `<type_definition>`          | **type** `<definition>`
| `<definition>`               | `<identifier>` **'{'** `<element_definition>` **'}'**
| `<element_definition>`       | **{** `<element_name>` `<element_type>` **; }<sup>+</sup>**
| `<element_name>`             | `<identifier>`
| `<element_type>`             | `<base_type>` **&#124;** `<collection_type>` **&#124;** `<user-defined_type>`
| `<default_value>`            | `<boolean>` **&#124;** `<integer>` **&#124;** `<float>` **&#124;** `<string>`
| `<basic_type>`                | **boolean &#124; integer &#124; float &#124; string**
| `<collection_type>`          | `<list_type>` **&#124;** `<set_type>` **&#124;** `<map_type>`
| `<list_type>`                | **list '&lt;'** `<element_type>` **'>'**
| `<set_type>`                 | **set '&lt;'** `<element_type>` **'>'**
| `<map_type>`                 | **map '&lt;'** `<element_type>` **,** `<element_type>` **'>'**
| `<user-defined_type>`        | `<identifier>`

## How to write relationships recognition rules

### How to define recognition rules

A recognition rule of requirement relationship is defined like:
```text
rule <rule_name> (<parameters_list>) -> boolean {
    <logic_body>
}
```
`<rule_name>` is an identifier.<br>
`<parameters_list>` is a list of parameter types, separated by `,`, like: `type1, type2, type3`.<br>
`<logic_body>` is the main part of the rule, specific syntax will be introduced later.<br>
Besides, for a 2 parameters rule with the same requirement type, user can define it as a reversible rule to eliminate the duplicated judgement.
For example, paras `(req1, req2)` and paras `(req2, req1)` are same to reversible rule.
The definition format is to replace `rule` at beginning with `revrule`.

### How to define functions

Users can also define some functions to simplify rules, like:
```text
function <function_name> (<parameters_list>) -> <return_type> {
    <field_name_1> : {
        <logic_body_2>
    };
    <field_name_2> : {
        <logic_body_2>
    };
    ...
}
```
`<function_name>` is identifier.<br>
`<return_type>` is the type of return value, which only be basic types and user-defined types.<br>
If `<return_type>` is a basic type, definition can be written like rule's as:
```text
function <function_name> (<parameters_list>) -> <basic_return_type> {
    <logic_body>
}
```
However, there is a difference of `<parameters_list>`: user can define different types of one parameter, like: `type1, type2 / type3, type4`.
It means the two parameters can be `type1` and `type2`, or `type3` and `type4`.

### How to write logic body

`<logic_body>` is consisted of a nested `<element>`, whose type is the `<return_type>`.<br>
An `<element>` has many conditions:
 * single element: parameter, variable, field of entity, list or map getting
 * operator statement: unary operator statement, binary operator statement, multi-para operator statement, loop statement, string statement
 * user-define function statement: statement of calling function defined by users

#### single element
 * literal type: such as: `true`, `666`, `0.1`, `"hello"`
 * parameter: `% <parameter_order_number> %`, for example, `%2%` means the 2nd parameter
 * variable: use the identifier defined before
 * field of entity: `<entity>.<field_name>`, for example, `ele.f` means field `f` in `ele`
 * list or map getting: `<collection> [ <index> ]`, for example, `list_demo[3]` means the 3rd element in `list_demo`

#### unary operator statement
The format is: `{ <unary_symbol> <element> }`, for example: `{-2}`, `{! boolean_value}`.<br>
**Notice**: '`{`' and '`}`' can not be omitted.<br>
All unary operators are:

| Operator         | Symbol  | Parameter Type   | Return Type       | Meaning                      |
|:----------------:|:-------:|:----------------:|:-----------------:|:----------------------------:|
| logical not      | !       | boolean          | boolean           | calculate `not` logic
| opposite number  | -       | integer / float  | same as parameter | calculate the opposite number
| collection size  | size_of | list / set / map | integer           | count the size of collection
| check nullable   | is_null | any user-defined type | boolean      | whether the entity is `null`

#### binary operator statement
The format is: `{ <element> <binary_symbol> <element> }`, for example: `{3 + 2}`, `{true or false}`.<br>
**Notice**: '`{`' and '`}`' can not be omitted.<br>
All binary operators are:

| Operator              | Symbol  | Parameters Type      | Return Type                           | Meaning           |
|:---------------------:|:-------:|:--------------------:|:-------------------------------------:|:-----------------:|
| number addition       | +       | both integer / float | float if has float, otherwise integer | calculate `plus`
| number subtraction    | -       | both integer / float | float if has float, otherwise integer | calculate `sub`
| number multiplication | *       | both integer / float | float if has float, otherwise integer | calculate `multiple`
| number division       | /       | both integer / float | float if has float, otherwise integer | calculate `divide`
| logical and           | and     | both boolean         | boolean                               | calculate `and` logic
| logical or            | or      | both boolean         | boolean                               | calculate `or` logic
| equal relation        | ==      | any same type        | boolean                               | whether the two elements are equal
| not equal relation    | !=      | any same type        | boolean                               | whether the two elements are not equal
| greater relation      | &gt;    | both integer / float | boolean                               | whether the former is greater than the latter
| less relation         | &lt;    | both integer / float | boolean                               | whether the former is less than the latter
| not less relation     | &gt;=   | both integer / float | boolean                               | whether the former is not less than the latter
| not greater relation  | &lt;=   | both integer / float | boolean                               | whether the former is not greater than the latter
| collection include    | include | same type of list / set / map | boolean                      | whether all elements of the latter are in the former
| collection in         | in      | element_type + collection_type | boolean                     | whether the element is in the collection (keys of map)
| set intersection      | inter   | same type of set     | set                                   | calculate the intersection of the two sets
| set union             | union   | same type of set     | set                                   | calculate the union of the two sets
| synonym words         | synonym | both string          | boolean                               | whether the two strings are synonyms
| antonym words         | antonym | both string          | boolean                               | whether the two strings are antonyms

#### multi-para operator statement
The format is: `<symbol> ( <element>, <element>, ... )`, in which `<element>`s are separated by `,`.
There can be at least one `<element>`. In this case, however, `<symbol>` has no effect.
Therefore, there are usually two or more `<element>`s.
For example, `+ (1, 2, 3, 4)`, `merge (list_1, liet_2)`.<br>
All multi-para operators are:

| Operator              | Symbol  | Parameters Type      | Return Type                           | Meaning           |
|:---------------------:|:-------:|:--------------------:|:-------------------------------------:|:-----------------:|
| summation of numbers  | +       | all integer / float  | float if has float, otherwise integer | calculate the summation of all numbers
| product of numbers    | *       | all integer / float  | float if has float, otherwise integer | calculate the product of all numbers
| maximum               | max     | all integer / float  | float if has float, otherwise integer | maximum of all numbers
| minimum               | min     | all integer / float  | float if has float, otherwise integer | minimum of all numbers
| logical and           | and     | all boolean          | boolean                               | calculate `and` of all boolean values
| logical or            | or      | all boolean          | boolean                               | calculate `or` of all boolean values
| collection merge      | merge   | same type of list / set / map | same as parameters           | merge all collections into one

#### user-define function statement
The format is: `<function_name> (<element>, <element>, ...)`, like multi-para operator statement, in which `<element>`s are separated by `,`.
The number of `<element>` depends on the definition of function.
For example, `function_1(para1, para2)`.
`<function_name>` is identifiers.<br>
**Notice**: functions should be defined before, then can be used.

#### loop statement
The loop format is: `for <quantifier> <loop_variable> <range> '(' <loop_statement> ')'`.
`<quantifier>` has 2 conditions: `any` (existential quantification) or `all`(universal quantification).
`<loop_variable>` is the identifier of variable in all the loop, represents every `<element>` in `<range>`.
`<range>` has 2 conditions:
 * `in <element>`: means every element in `<element>`, where `<element>` should be collection type.
 * `from <begin> to <end>`: means every integer in [`<begin>`, `<end>`),
   where `<begin>` and `<end>` should be integer type, and `<begin>` should less than `<end>`.

#### string statement
The string format has 2 conditions:
 * `substring '(' <string>, <begin>, <end> ')'`: the substring of `<string>` of index of [`<begin>`, `<end>`),
   where `<string>` is the string to be cut, `<begin>` and `<end>` should be integer type, and `<begin>` should less than `<end>`.
 * `find '(' <range>, <from_index>, <target> ')'`: the index of `<target>` in `<range>` from index `<from_index>`,
   where `<range>` and `<target>` are string type, and `<from_index>` is integer type.
   If not exists, return `-1` instead. For example, `find ("abab", 1, "ab")` return `2`.

## How to write requirements
Requirements and entities are written in JSON format, which should satisfy the definitions of their models.
The basic format is: `[ <entity>, <entity>, ... ]`, in where `<entity>`s are seperated by `,` and has 4 conditions:
 * basic element:
   - boolean: JSON boolean value, `true` / `false`
   - integer or float: JSON number value, like `3` or `2.15`
   - string: JSON string: like `"hello"`
 * collection type:
   - list: `{ "[]": [ <entity>, <entity>, ... ] }`
   - set:  `{ "()": [ <entity>, <entity>, ... ] }`
   - map: 
     ```text
     {
         "{}": [
             {"K": <entity>,
              "V": <entity>},
             ...
         ]
     }
     ```
 * entity type: includes requirement and entity. Basic format is:
   ```text
   {
       "#": <string of entity ID>,
       "*": <entity type>,
       ":": <string for comment>,
       <field_name> : <field_entity>,
       <field_name> : <field_entity>,
       ...
   }
   ```
   In this, `<entity_ID>` is a JSON string, represents the ID of entity, using in entity-link below.
   It should be unique for each entity in a single type, yet can be duplicated in different types.  
   `<field_name>` is the identifier of field, and `<field_entity>` is also an `<entity>`.  
   Any field has no assignment in this way is initialized with default value.
 * entity-link: a way to refer other entity using its ID. Basic format is: `{ <entity_type> : <entity_ID> }`.
   In this, `<entity_type>` is the type of the linked entity, and `<entity_ID>` is its ID.

If a field of entity is not specified, it should be the default, see [`this`](#how-to-write-requirements-models).

## Structure of Main Directory
```
├── generated (auto-generated classes, including classes of customized types and class of rule)
│   ├── <requirement dataset name>
│   │   ├── $<customized type name>.java
│   │   ├── ... ...
│   │   └── $rule$.java
│   └── ... ...
├── java
│   ├── api
│   │   └── Main.java
│   ├── basicTypes
│   │   ├── BaseEntity.java
│   │   ├── primitive
│   │   │   ├── BasePrimitiveEntity.java
│   │   │   ├── BoolEntity.java
│   │   │   ├── IntEntity.java
│   │   │   ├── FloatEntity.java
│   │   │   └── StringEntity.java
│   │   └── collection
│   │       ├── BaseCollectionEntity.java
│   │       ├── ListEntity.java
│   │       ├── SetEntity.java
│   │       └── MapEntity.java
│   ├── codeGenerator
│   │   ├── JavaHeaderGenerator.java
│   │   ├── LogicParser.java
│   │   ├── ModelJsonParser.java
│   │   └── RuleParser.java
│   ├── dynamics
│   │   ├── Builder.java
│   │   ├── Compiler.java
│   │   └── TypeManager.java
│   ├── exceptions
│   │   ├── TokenInvalidException.java
│   │   └── TypeInvalidException.java
│   ├── grpc
│   │   ├── Request.java
│   │   ├── Response.java
│   │   ├── RequestOrBuilder.java
│   │   ├── ResponseOrBuilder.java
│   │   ├── Requirement.java
│   │   ├── StructurationGrpc.java
│   │   └── Client.java
│   ├── judge
│   │   └── Processor.java
│   ├── languageParser
│   │   ├── BaseParser.java
│   │   ├── ModelTextParser.java
│   │   └── RuleTextParser.java
│   ├── reqParser
│   │   ├── EntityParser.java
│   │   └── RequirementParser.java
│   └── util
│       ├── FormatsConsts.java
│       ├── ModeEnum.java
│       ├── OperatorConsts.java
│       ├── PathCosts.java
│       ├── TextReader.java
│       ├── ThesaurusReader.java
│       └── TypeEnum.java
├── proto: Protobuf files
│   └── requirement.proto (defining natrual language requirement request and structuralization response)
└── resources
    ├── log
    │   ├── <log name>.log
    │   └── log4j2.xml
    ├── entities
    │   ├── entity_schema.json
    │   ├── <requirement dataset name>
    │   │   ├── <requirements dataset>.txt
    │   │   ├── <requirement type>.json
    │   │   ├── ... ...
    │   │   ├── <entity name>.json
    │   │   └── ... ...
    │   └── ... ...
    ├── models
    │   ├── model_schema.json
    │   ├── <requirement dataset name>
    │   │   ├── <file name>.r4dl
    │   │   ├── ... ...
    │   │   ├── <file name>.json
    │   │   └── ... ...
    │   └── ... ...
    ├── rules
    │   ├── rule_schema.json
    │   ├── <requirement dataset name>
    │   │   ├── <file name>.r4dl
    │   │   ├── ... ...
    │   │   ├── <file name>.json
    │   │   └── ... ...
    │   └── ... ...
    ├── thesaurus
    │   ├── antonym (dictionary of antonym words)
    │   └── synonym (dictionary of synonym words)
    └── properties
        ├── config.properties
        ├── formats.properties
        └── operators.properties
```
