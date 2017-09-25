# ApplicableClasses

x400 only one applicable class

Except for:

- Pset_CoveringFlooring

```
<ApplicableClasses>
  <ClassName>IfcCovering/FLOORING</ClassName>
  <ClassName />
</ApplicableClasses>
```

Note: problem with the empty tag


- Pset_DoorWindowGlazingType
- Pset_DoorWindowShadingType

```
<ApplicableClasses>
  <ClassName>IfcDoor</ClassName>
  <ClassName> IfcWindow</ClassName>
</ApplicableClasses>
```

Note: problem with initial space character

- Pset_AirSideSystemInformation
- Pset_SpaceFireSafetyRequirements
- Pset_SpaceLightingRequirements
- Pset_SpaceOccupancyRequirements
- Pset_SpaceThermalRequirements
- IfcCovering/FLOORING: Covering with predefined type of Flooring

```
<ClassName>IfcSpace</ClassName>
<ClassName> IfcSpatialZone</ClassName>
<ClassName> IfcZone</ClassName>
```

Note: problem with initial space character


It is possible to define exceptions with SPARQL-Generate if necessary.

Done:
 - remove empty spaces and empty tags
 - generalization of IfcDoor IfcWindow --> bot:Element
 - generalization of IfcSpace IfcSpatialZone IfcZone --> bot:Zone

## what to do with those classes that have their name like IfcBase/TYPE


option 1 anonymous OWL Class:

-> implies that we have a superclass for Classes, and a superclass for PredefinedType

[ owl:intersectionOf ( <https://w3id.org/product/Covering>  [
  owl:onProperty :hasPredefinedType ;
  owl:allValuesFrom <https://w3id.org/product/Flooring.PredefinedType#FLOORING>
  ] .

option 2 one OWL Class:

- <https://w3id.org/product/Covering%2FFLOORING> 
- <https://w3id.org/product/Covering#FLOORING>  ---------> CHOSEN for now


# Type property single values ---> DatatypeProperties

```
<PropertyType>
  <TypePropertySingleValue>
    <DataType type="IfcText" />
  </TypePropertySingleValue>
</PropertyType>
```

## List of Ifc[a-zA-Z]+Measure

 ---> property is ObjectProperty  ---> see with Walter
 ---> OR property is DatatypeProperty with datatype cdt:ucum      "13.354 m3"^^cdt:ucum
 ---> OR property is DatatypeProperty with datatype [a-z][a-zA-Z]*  ------> CHOSEN for now

- SectionModulusMeasure
- DynamicViscosityMeasure
- VaporPermeabilityMeasure
- ForceMeasure
- ThermodynamicTemperatureMeasure
- RatioMeasure
- ElectricResistanceMeasure
- ThermalExpansionCoefficientMeasure
- PositiveLengthMeasure
- ModulusOfElasticityMeasure
- ElectricVoltageMeasure
- TimeMeasure
- PositiveRatioMeasure
- VolumetricFlowRateMeasure
- PositivePlaneAngleMeasure
- ThermalResistanceMeasure
- EnergyMeasure
- HeatingValueMeasure
- IlluminanceMeasure
- SpecificHeatCapacityMeasure
- ThermalConductivityMeasure
- LinearVelocityMeasure
- MolecularWeightMeasure
- CountMeasure
- PowerMeasure
- PlanarForceMeasure
- PHMeasure
- ElectricCurrentMeasure
- NormalisedRatioMeasure
- AreaDensityMeasure
- MoistureDiffusivityMeasure
- WarpingConstantMeasure
- PressureMeasure
- VolumeMeasure
- TorqueMeasure
- MassDensityMeasure
- AreaMeasure
- MassFlowRateMeasure
- LuminousFluxMeasure
- TemperatureRateOfChangeMeasure
- IsothermalMoistureCapacityMeasure
- MassMeasure
- FrequencyMeasure
- IonConcentrationMeasure
- PlaneAngleMeasure
- RotationalFrequencyMeasure
- HeatFluxDensityMeasure
- MomentOfInertiaMeasure
- LengthMeasure
- ThermalTransmittanceMeasure
- MassPerLengthMeasure
- NonNegativeLengthMeasure

translated into <https://w3id.org/measurement/{  concat( lcase( substr( ?type, 1, 2) ) , replace( substr( ?type, 2 ) , "Measure" , "" ) ) }>
 example: MassFlowRateMeasure -->  <https://w3id.org/measurement/massFlowRate>

```
?property rdfs:range <https://w3id.org/measurement/{ ?type }> .
```

## Other types ---> property is DatatypeProperty

- Text  --> range xsd:string, but is also a subProperty of rdfs:comment
- Label --> range xsd:string, but is also a subProperty of rdfs:label
- Identifier --> range xsd:string, but is also a subProperty of dcterms:identifier
- Boolean --> xsd:boolean
- Logical --> xsd:boolean
- Integer --> xsd:int
- Real --> xsd:float
- Date --> xsd:date
- DateTime --> xsd:dateTime
- Time --> xsd:time 
- Duration --> xsd:duration
- ComplexNumber --> <https://w3id.org/measurement/complexNumber> 
    -  "3 + 4i"^^cdt:complex 
    -  "3 + 4j"^^cdt:complex or 
    -  "3534 e^(qqsdf+ 3534j)"^^cdt:complex or 


# TypePropertyReferenceValue ---> ObjectProperties

## with reftype=""

when reftype is empty then the range of the object property is the type of the DataType object

45 such property types:

- 1 DataType with type= IfcOrganization      --->  foaf:Organization
- 1 DataType with type= IfcPerson            --->  foaf:Person
- 16 DataType with type= IfcTimeSeries       --->  <https://w3id.org/measurement/TimeSeries>
- 25 Datatype with type = IfcMaterialDefinition -> <https://w3id.org/product/MaterialDefinition>
- 2 DataType with type= IfcExternalReference --->  nothing

## with reftype="{ ?measureType }"

219 matches, all have Datatype with type= IfcTimeSeries

```
<{ ?name }> rdfs:range <https://w3id.org/measurement/TimeSeries> , [ 
   owl:onProperty <https://w3id.org/measurement/seriesOf> ;
   owl:allValuesFrom measurement:IonConcentrationMeasure ] .
```

---> CHOSEN for now, waiting for better proposals

# TypePropertyEnumeratedValue

344 matches

some common options are:

- UNSET (319 matches)
- UNKNOWN (323 matches)
- OTHER (325 matches)

Example of enumerated value without UNSET, OTHER, UNKNOWN: 

```
<EnumList name="PEnum_BackInletPatternType">
  <EnumItem>NONE</EnumItem>
  <EnumItem>1</EnumItem>
  <EnumItem>2</EnumItem>
  <EnumItem>3</EnumItem>
  <EnumItem>4</EnumItem>
  <EnumItem>12</EnumItem>
  <EnumItem>13</EnumItem>
  <EnumItem>14</EnumItem>
  <EnumItem>23</EnumItem>
  <EnumItem>24</EnumItem>
  <EnumItem>34</EnumItem>
  <EnumItem>123</EnumItem>
  <EnumItem>124</EnumItem>
  <EnumItem>134</EnumItem>
  <EnumItem>234</EnumItem>
  <EnumItem>1234</EnumItem>
</EnumList>
```

Note: some EnumItems start with a \n !!!


Proposal for dealing with EnumItems:

```
- FIXED    --> a class called AudioVisualAmplifierType#FIXED
- VARIABLE --> a class called AudioVisualAmplifierType#VARIABLE
- OTHER    --> a class called AudioVisualAmplifierType#OTHER
- NOTKNOWN --> the triple is not there
```

TODO: ask this afternoon who is using OTHER vs NOTKNOWN


# TypePropertyBoundedValue
see also http://www.buildingsmart-tech.org/ifc/IFC2x3/TC1/html/ifcpropertyresource/lexical/ifcpropertyboundedvalue.htm

118 matches

- 109 Ifc[a-zA-Z]+Measure
- 3 IfcInteger
- 1 IfcDuration
- 5 IfcReal


 - have one literal with concatanation of the two values (then custom sparql functions to extract these values individually)
 - have two individual datatype properties
 - have one datatype property  -->- and two evaluations of the value for that property -> one would be the minimal and one would be the maximal



# TypePropertyTableValue

63 matches, all are emtpy tags

# TypePropertyListValue

19 matches, all with value for XPath expression `TypePropertyListValue/ListValue/DataType/@type`:
- 6 IfcLabel
- 1 IfcIdentifier
- 3 IfcElectricCurrentMeasure
- 1 IfcTimeMeasure
- 1 IfcText
- 1 IfcFrequencyMeasure
- 6 IfcLengthMeasure

lists of literals usually, that can be turned into a specific literal with a specific datatype ---> for example "List (() \"10.7 m\", \"1.5 mm\", \"2.0 mm\" ))"^^cdt:ucum





# SPARQL-Generate

rules to generate RDF out of:
 -  an ontology (the generated one, with the list of Properties)
 -  some document (the STEP)