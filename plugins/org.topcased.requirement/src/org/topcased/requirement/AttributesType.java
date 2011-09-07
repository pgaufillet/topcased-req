/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.topcased.requirement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Attributes Type</b></em>', and
 * utility methods for working with them. <!-- end-user-doc -->
 * @see org.topcased.requirement.RequirementPackage#getAttributesType()
 * @model
 * @generated
 */
public enum AttributesType implements Enumerator {
    /**
     * The '<em><b>Text</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #TEXT_VALUE
     * @generated
     * @ordered
     */
    TEXT(0, "Text", "Text"),

    /**
     * The '<em><b>Object</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #OBJECT_VALUE
     * @generated
     * @ordered
     */
    OBJECT(1, "Object", "Object"),

    /**
     * The '<em><b>Allocate</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #ALLOCATE_VALUE
     * @generated
     * @ordered
     */
    ALLOCATE(2, "Allocate", "Allocate"),

    /**
     * The '<em><b>Link</b></em>' literal object.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #LINK_VALUE
     * @generated
     * @ordered
     */
    LINK(3, "Link", "Link");

    /**
     * The '<em><b>Text</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Text</b></em>' literal object isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @see #TEXT
     * @model name="Text"
     * @generated
     * @ordered
     */
    public static final int TEXT_VALUE = 0;

    /**
     * The '<em><b>Object</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Object</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OBJECT
     * @model name="Object"
     * @generated
     * @ordered
     */
    public static final int OBJECT_VALUE = 1;

    /**
     * The '<em><b>Allocate</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Allocate</b></em>' literal object isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ALLOCATE
     * @model name="Allocate"
     * @generated
     * @ordered
     */
    public static final int ALLOCATE_VALUE = 2;

    /**
     * The '<em><b>Link</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Link</b></em>' literal object isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LINK
     * @model name="Link"
     * @generated
     * @ordered
     */
    public static final int LINK_VALUE = 3;

    /**
     * An array of all the '<em><b>Attributes Type</b></em>' enumerators.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static final AttributesType[] VALUES_ARRAY = new AttributesType[] {TEXT, OBJECT, ALLOCATE, LINK,};

    /**
     * A public read-only list of all the '<em><b>Attributes Type</b></em>' enumerators.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static final List<AttributesType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Attributes Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static AttributesType get(String literal)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            AttributesType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Attributes Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    public static AttributesType getByName(String name)
    {
        for (int i = 0; i < VALUES_ARRAY.length; ++i)
        {
            AttributesType result = VALUES_ARRAY[i];
            if (result.getName().equals(name))
            {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Attributes Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static AttributesType get(int value)
    {
        switch (value)
        {
            case TEXT_VALUE:
                return TEXT;
            case OBJECT_VALUE:
                return OBJECT;
            case ALLOCATE_VALUE:
                return ALLOCATE;
            case LINK_VALUE:
                return LINK;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private AttributesType(int value, String name, String literal)
    {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public int getValue()
    {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getName()
    {
        return name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral()
    {
        return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        return literal;
    }

} // AttributesType
