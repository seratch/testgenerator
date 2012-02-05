package example.typeparameters

class WithTypeParameters[T]

trait WithTypeParametersTrait[T]

class StructuralType[Foo <: { def toString(): String }]

trait StructuralTypeTrait[Foo <: { def toString(): String }]

class CoVariant[+T]

trait CoVariantTrait[+T]

class ContraVariant[-T]

trait ContraVariantTrait[-T]

class UpperBound[T <: Any]

trait UpperBoundTrait[T <: Any]

class LowerBound[T >: TraversableOnce[String]]

trait LowerBoundTrait[T >: TraversableOnce[String]]
