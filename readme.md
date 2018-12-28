*What is it?*

Its a JSON config file editor. At times we need to be able to change few config file values but don't want to make the end use suffer using a text editor since they might make a mistake (new line, space, comma etc) and also we may want to validate values supplied by user.

*How does it work?*

It reads the given json file and feeds the values from this json file (as specified in formdef file) to the specified HTML. Any kind of validation can be done HTML using java script. The data from form will be saved to the same JSON file using app_save function.

*Example*

```
java -cp target\configeditor-0.0.1-SNAPSHOT-shaded.jar org.himalay.config.Editor --htmlForm  examples\form.html --configJsonFile examples\confData.json --formDef examples\formdef.json
```