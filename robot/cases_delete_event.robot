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
TC-DELETE-EVENT-001 - Validar impossibilidade de exclusão de evento com ingressos comprados

    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_DELETE}
    [Documentation]    Validar não exclusão de evento com ingresso comprado
    ${event_id}    ${ticket_type_id}=    Criar Evento e Ticket Type
    ${ticket_name}=    FakerLibrary.Word
    ${response}=    Executar_Compra_Ticket    ${ticket_type_id}    1    200
    Executar Delete Event    ${event_id}    ${STATUS_409}

TC-DELETE-EVENT-002 - Validar exclusão de evento com Ticket Type sem ingressos comprados

    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_DELETE}
    [Documentation]    Validar não exclusão de evento com ingresso comprado
    ${event_id}    ${ticket_type_id}=    Criar Evento e Ticket Type
    ${response}=    Executar_Compra_Ticket    ${ticket_type_id}    1    200
    Executar Delete Event    ${event_id}    ${STATUS_204}

TC-DELETE-EVENT-003 - Validar exclusão de evento sem ticket type cadastrado

    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_DELETE}
    [Documentation]    Validar não exclusão de evento com ingresso comprado
    ${ticket_name}=   FakerLibrary.Word
    &{payload}=    Gerar Payload Sucesso Completo Aleatório
    ${event_id}    ${response}=    Executar POST e Validar Status PATCH   ${payload}    ${STATUS_201}
    Executar Delete Event    ${event_id}    ${STATUS_204}

TC-DELETE-EVENT-004 - Validar impossibilidade de exclusão de evento com id inexistente
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_DELETE}
    [Documentation]    Validar exclusão de evento com id inexistente
    Executar Delete Event    ${uuid_inexistente}    ${STATUS_404}