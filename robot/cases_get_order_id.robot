*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Test Setup    Criar Sessao API

*** Variables ***
${TAG_ALL_TESTS}    smoke    regression
${TAG_SUCCESS}      success
${TAG_FAILURE}      failure
${TAG_GET}          get

*** Test Cases ***

GET-ORDER-ID-001 - Validar obtenção de pedido por ID existente
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar obtenção de pedido por ID existente

    ${event_id}    ${ticket_type_id}=    Criar Evento e Ticket Type
    ${orderId}    ${response}=    Executar Compra Ticket e Capturar OrderId    ${ticket_type_id}    1    200
    Executar Get Order By ID    ${orderId}    ${STATUS_200}

GET-ORDER-ID-002 - Validar impossibilidade de obtenção de pedido por ID inexistente
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar obtenção de pedido por ID existente

    Executar Get Order By ID    ${uuid_inexistente}    ${STATUS_404}

GET-ORDER-ID-003 - Validar impossibilidade de obtenção de pedido por ID em formato inválido
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar obtenção de pedido por ID existente

    Executar Get Order By ID    ${uuid_invalido}    ${STATUS_400}