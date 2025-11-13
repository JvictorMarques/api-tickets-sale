*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Test Setup    Criar Sessao API

*** Variables ***
${TAG_ALL_TESTS}    smoke    regression
${TAG_SUCCESS}      success
${TAG_FAILURE}      failure
${TAG_DELETE}       delete


*** Test Cases ***
DELETE-EVENT-TICKET-001 - Validar impossibilidade de exclusão de tipo de ticket com ingressos comprados

    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_DELETE}
    [Documentation]    Validar não exclusão de tipo de ticket com ingresso comprado
    ${ticket_type_id}=    Criar_Ticket_Type_E_Capturar_ID
    ${response}=    Executar_Compra_Ticket    ${ticket_type_id}    1    200
    Validar_Response_Compra_Sucesso    ${response}
    Sleep     5s
    Executar Delete Ticket Type    ${ticket_type_id}    ${STATUS_409}

DELETE-EVENT-TICKET-002 - Validar possibilidade de exclusão de tipo de ticket sem ingressos comprados

    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_DELETE}
    [Documentation]    Validar exclusão de ticket comprado
    ${ticket_type_id}=    Criar_Ticket_Type_E_Capturar_ID
    Executar Delete Ticket Type    ${ticket_type_id}    ${STATUS_204}

DELETE-EVENT-TICKET-003 - Validar impossibilidade de exclusão de tipo de ticket com id inexistente

    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_DELETE}
    [Documentation]    Validar exclusão de ticket comprado
    Executar Delete Ticket Type    ${uuid_inexistente}    ${STATUS_404}

DELETE-EVENT-TICKET-004 - Validar impossibilidade de exclusão de tipo de ticket com id inválido

    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_DELETE}
    [Documentation]    Validar exclusão de ticket comprado
    Executar Delete Ticket Type    ${uuid_invalido}    ${STATUS_400}