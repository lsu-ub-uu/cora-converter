module se.uu.ub.cora.converter {
	requires transitive se.uu.ub.cora.logger;
	requires transitive se.uu.ub.cora.data;

	exports se.uu.ub.cora.converter;

	uses se.uu.ub.cora.converter.ConverterFactory;
}