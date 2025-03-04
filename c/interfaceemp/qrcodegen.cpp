#include "qrcodegen.h"

class qrcodegenData : public QSharedData
{
public:

};

qrcodegen::qrcodegen() : data(new qrcodegenData)
{

}

qrcodegen::qrcodegen(const qrcodegen &rhs) : data(rhs.data)
{

}

qrcodegen &qrcodegen::operator=(const qrcodegen &rhs)
{
    if (this != &rhs)
        data.operator=(rhs.data);
    return *this;
}

qrcodegen::~qrcodegen()
{

}
