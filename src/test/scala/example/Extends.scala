package example

class Something

trait Readable

class ExtendedSomething extends Something

class ReadableSomething extends Something with Readable

trait SObject

object ExtendedSObject extends SObject

object ReadableSObject extends SObject with Readable

trait ReadableTrait extends SObject with Readable