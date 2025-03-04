#ifndef QRCODEGEN_H
#define QRCODEGEN_H

#include <QDeclarativeItem>
#include <QMainWindow>
#include <QObject>
#include <QQuickItem>
#include <QSharedDataPointer>
#include <QWidget>

class qrcodegenData;

class qrcodegen
{
    Q_OBJECT
public:
    qrcodegen();
    qrcodegen(const qrcodegen &);
    qrcodegen &operator=(const qrcodegen &);
    ~qrcodegen();

private:
    QSharedDataPointer<qrcodegenData> data;
};

#endif // QRCODEGEN_H
