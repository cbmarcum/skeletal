package uk.co.cacoethes.lazybones.config

import uk.co.cacoethes.lazybones.packagesources.ArtifactoryPackageSource

/**
 * Created by pledbrook on 09/08/2014.
 */
class Converters {
    static final Map<Class, Converter> CONVERTER_MAP = Collections.unmodifiableMap([
            (Object): new ObjectConverter(),
            (Boolean): new BooleanConverter(),
            (Integer): new IntegerConverter(),
            (String): new StringConverter(),
            (ArtifactoryPackageSource.ArtifactoryConfig): new ArtifactoryConfigConverter(),
            (URI): new UriConverter()])

    static <T> Converter<T> getConverter(Class<T> theClass) {
        if (theClass.isArray()) return new ListConverter(theClass.componentType)

        return CONVERTER_MAP.get(theClass)
    }
}
