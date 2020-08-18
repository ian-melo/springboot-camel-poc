<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output omit-xml-declaration="no" version="1.0" standalone="yes" indent="no"/>

<xsl:template match="/agenda">
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:catalogo="http://registro.com.br/catalogo">
    <soapenv:Body>
        <catalogo:catalogo>
            <catalogo:itens>
                <xsl:if test="autor/email!='' or autor/telefone!=''"><catalogo:item>
                    <xsl:if test="autor/email!=''"><catalogo:email><xsl:value-of select="autor/email"/></catalogo:email></xsl:if>
                    <xsl:if test="autor/telefone!=''"><catalogo:telefone><xsl:value-of select="autor/telefone"/></catalogo:telefone></xsl:if>
                    <catalogo:data><xsl:value-of select="/agenda/dataCriacao"/></catalogo:data>
                </catalogo:item></xsl:if>
            <xsl:for-each select="contatos/contato">
                <xsl:if test="email!='' or telefone!=''"><catalogo:item>
                    <xsl:if test="email!=''"><catalogo:email><xsl:value-of select="email"/></catalogo:email></xsl:if>
                    <xsl:if test="telefone!=''"><catalogo:telefone><xsl:value-of select="telefone"/></catalogo:telefone></xsl:if>
                    <catalogo:data><xsl:value-of select="/agenda/dataCriacao"/></catalogo:data>
                </catalogo:item></xsl:if>
            </xsl:for-each>
            </catalogo:itens>
        </catalogo:catalogo>
    </soapenv:Body>
</soapenv:Envelope>
</xsl:template>

</xsl:stylesheet>