# Using the Silk Library

The Silk library was designed to be as easy as possible to use. Once you have a Silk source code file (program), you
need to compile it to bytecode so that it can be executed.

## Compiler Class

The first step to compile a program is to create an instance of the `Compiler` class.

In order to allow a program to do useful things with your host application, use the `Compiler.RegisterFunction()`
and `Compiler.RegisterVariable()` methods to add your own functions and variables, which will be available to the Silk
source code. This will give the language the ability to perform the tasks you provide that are specific to the host
application's domain.

Next, call the `Compiler.Compile()` method. If it returns false, the compile failed and you can use the `Errors`
property to access the compile errors. These errors include a description of each error along with the line number where
the error occurred. If the compile succeeds, the `Compiler.Compile()` method returns true and creates an instance of
the `CompiledProgram` class.

This is demonstrated in the following example.

```cs
Compiler compiler = new Compiler();

// Register intrinsic functions
compiler.RegisterFunction("Print", 0, Function.NoParameterLimit);
compiler.RegisterFunction("Color", 1, 2);
compiler.RegisterFunction("ClearScreen", 0, 0);
compiler.RegisterFunction("ReadKey", 0, 0);

// Register intrinsic variables
foreach (var color in Enum.GetValues(typeof(ConsoleColor)))
    compiler.RegisterVariable(color.ToString(), new Variable((int)color));

if (compiler.Compile(path, out CompiledProgram program))
{
    // Success
    Console.WriteLine("Compile succeeded.");
}
else
{
    // Failure: display compile errors
    Console.WriteLine("Compile failed.");
    Console.WriteLine();
    foreach (Error error in compiler.Errors)
        Console.WriteLine(error.ToString());
}
```

## CompiledProgram Class

As the name implies, the `CompiledProgram` object created by `Compiler.Compile()` contains the compiled code.

You can use this class' `Save()` and `Load()` methods to save a compiled program to a file, and load a compiled program
from a file. This allows you to load a previously compiled program and run it without needing to compile it each time.

## Runtime Class

To run a `CompiledProgram`, create an instance of the `Runtime` class and pass the `CompiledProgram` to
the `Runtime.Execute()` method.

The `Runtime` class exposes three events: `Begin`, `Function` and `End`. The `Begin` event is called when the program
starts to run. The `BeginEventArgs` argument contains a property called `UserData`. You can use this property to store
any contextual data in your application and this same object will be passed to the other `Runtime` events.

The `Function` event is called when the program executes a call to a function that you registered
with `Compiler.RegisterFunction()`. The `FunctionEventArgs` includes a `Name` property that contains the name of the
function. Note that while the language is not case-sensitive, the name of the function passed to the `Function` event
handler will always have the same case as the name you passed to `Compiler.RegisterFunction()`. It also includes
a `Parameters` property, which is an array of arguments that were passed to the function. And it also includes
a `ReturnValue` property, which specifies the function's return value.

This is demonstrated by the following example.

```cs
private static void RunProgram(CompiledProgram program)
{
    Runtime runtime = new Runtime();
    runtime.Begin += Runtime_Begin;
    runtime.Function += Runtime_Function;
    runtime.End += Runtime_End;

    Variable result = runtime.Execute(program);

    Console.WriteLine();
    Console.WriteLine($"Program ran successfully with exit code {result}.");
}

private static void Runtime_Begin(object sender, BeginEventArgs e)
{
    e.UserData = this;
}

private static void Runtime_Function(object sender, FunctionEventArgs e)
{
    switch (e.Name)
    {
        case "Print":
            Console.WriteLine(string.Join("", e.Parameters.Select(p => p.ToString())));
            break;
        case "Color":
            Debug.Assert(e.Parameters.Length >= 1);
            Debug.Assert(e.Parameters.Length <= 2);
            if (e.Parameters.Length >= 1)
                Console.ForegroundColor = (ConsoleColor)e.Parameters[0].ToInteger();
            if (e.Parameters.Length >= 2)
                Console.BackgroundColor = (ConsoleColor)e.Parameters[1].ToInteger();
            break;
        case "ClearScreen":
            Console.Clear();
            break;
        case "ReadKey":
            e.ReturnValue.SetValue(Console.ReadKey().KeyChar);
            break;
        default:
            Debug.Assert(false);
            break;
    }
}

private static void Runtime_End(object sender, EndEventArgs e)
{
}
```

Note that the `Variable` returned from `Runtime.Execute()` contains the value returned from the program's `main()`
function.

## Tip if you have Many Functions

If you define a lot of functions, you may find the large `switch` statement cumbersome. Here is an example of using
the `Dictionary` class to create a handler lookup table. This way, each of your function handlers can be a separate
method.

```cs
private static Dictionary<string, Action<Variable[], Variable>> FunctionLookup = new Dictionary<string, Action<Variable[], Variable>>
{
    ["Print"] = Print,
    ["Color"] = Color,
    ["ClearScreen"] = ClearScreen,
    ["ReadKey"] = ReadKey,
};

private static void Runtime_Function(object sender, FunctionEventArgs e)
{
    if (FunctionLookup.TryGetValue(e.Name, out Action<Variable[], Variable> action))
        action(e.Parameters, e.ReturnValue);
    else
        Debug.Assert(false);    // Unknown function
}

private static void Print(Variable[] parameters, Variable returnValue)
{
    Console.WriteLine(string.Join("", parameters.Select(p => p.ToString())));
}

private static void Color(Variable[] parameters, Variable returnValue)
{
    Debug.Assert(parameters.Length >= 1);
    Debug.Assert(parameters.Length <= 2);
    if (parameters.Length >= 1)
        Console.ForegroundColor = (ConsoleColor)parameters[0].ToInteger();
    if (parameters.Length >= 2)
        Console.BackgroundColor = (ConsoleColor)parameters[1].ToInteger();
}

private static void ClearScreen(Variable[] parameters, Variable returnValue)
{
    Console.Clear();
}

private static void ReadKey(Variable[] parameters, Variable returnValue)
{
    returnValue.SetValue(Console.ReadKey().KeyChar);
}
```
